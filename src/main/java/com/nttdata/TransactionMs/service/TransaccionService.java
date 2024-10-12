package com.nttdata.TransactionMs.service;


import com.nttdata.TransactionMs.exception.TransaccionException;
import com.nttdata.TransactionMs.model.HistorialTransaccion;
import com.nttdata.TransactionMs.model.TransaccionDeposito;
import com.nttdata.TransactionMs.model.TransaccionRetiro;
import com.nttdata.TransactionMs.model.TransaccionTransferencia;
import com.nttdata.TransactionMs.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private RestTemplate restTemplate;  // Inyectamos RestTemplate

    private static final String CUENTAS_MS_BASE_URL = "http://localhost:8081/cuentas";  // Cambia la URL según tu configuración

    private Long obtenerIdPorNumeroCuenta(String numeroCuenta) {
        Map[] cuentas = restTemplate.getForObject(CUENTAS_MS_BASE_URL, Map[].class);

        return Arrays.stream(cuentas)
                .filter(cuenta -> numeroCuenta.equals(cuenta.get("numeroCuenta")))
                .map(cuenta -> {
                    Object id = cuenta.get("id");
                    if (id instanceof Integer) {
                        return ((Integer) id).longValue(); // Convertir de Integer a Long
                    } else if (id instanceof Long) {
                        return (Long) id; // Ya es Long
                    } else {
                        throw new IllegalArgumentException("Tipo de ID no soportado: " + id.getClass().getName());
                    }
                })
                .findFirst()
                .orElseThrow(() -> new TransaccionException("Cuenta no encontrada", HttpStatus.NOT_FOUND));
    }


    public ResponseEntity<Void> registrarDeposito(TransaccionDeposito deposito) {
        Long cuentaId = obtenerIdPorNumeroCuenta(deposito.getNumeroCuenta().toString());
        String url = CUENTAS_MS_BASE_URL + "/" + cuentaId + "/depositar";

        HttpEntity<TransaccionDeposito> requestEntity = new HttpEntity<>(deposito);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            HistorialTransaccion transaccion = new HistorialTransaccion()
                    .tipo(HistorialTransaccion.TipoEnum.DEPOSITO)
                    .monto(deposito.getMonto())
                    .cuentaDestino(deposito.getNumeroCuenta())
                    .fecha(OffsetDateTime.now());
            transaccionRepository.save(transaccion);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }


    public ResponseEntity<Void> registrarRetiro(TransaccionRetiro retiro) {
        Long cuentaId = obtenerIdPorNumeroCuenta(retiro.getNumeroCuenta().toString());
        String url = CUENTAS_MS_BASE_URL + "/" + cuentaId + "/retirar";
        HttpEntity<TransaccionRetiro> requestEntity = new HttpEntity<>(retiro);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                HistorialTransaccion transaccion = new HistorialTransaccion()
                        .tipo(HistorialTransaccion.TipoEnum.RETIRO)
                        .monto(retiro.getMonto())
                        .cuentaOrigen(retiro.getNumeroCuenta())
                        .fecha(OffsetDateTime.now());
                transaccionRepository.save(transaccion);
                return ResponseEntity.ok().build();
            }

        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            throw new TransaccionException(errorMessage, HttpStatus.BAD_REQUEST);
        }

        throw new TransaccionException("Error desconocido al procesar el retiro", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Void> registrarTransferencia(TransaccionTransferencia transferencia) {
        Long cuentaOrigenId = obtenerIdPorNumeroCuenta(transferencia.getCuentaOrigen().toString());
        Long cuentaDestinoId = obtenerIdPorNumeroCuenta(transferencia.getCuentaDestino().toString());

        // URL para debitar de la cuenta origen
        String urlDebitar = CUENTAS_MS_BASE_URL + "/" + cuentaOrigenId + "/retirar";
        HttpEntity<TransaccionTransferencia> requestEntityDebitar = new HttpEntity<>(transferencia);
        ResponseEntity<Void> responseDebitar = restTemplate.exchange(urlDebitar, HttpMethod.PUT, requestEntityDebitar, Void.class);

        if (responseDebitar.getStatusCode().is2xxSuccessful()) {
            // URL para acreditar en la cuenta destino
            String urlAcreditar = CUENTAS_MS_BASE_URL + "/" + cuentaDestinoId + "/depositar";
            HttpEntity<TransaccionTransferencia> requestEntityAcreditar = new HttpEntity<>(transferencia);
            ResponseEntity<Void> responseAcreditar = restTemplate.exchange(urlAcreditar, HttpMethod.PUT, requestEntityAcreditar, Void.class);

            if (responseAcreditar.getStatusCode().is2xxSuccessful()) {
                HistorialTransaccion transaccion = new HistorialTransaccion()
                        .tipo(HistorialTransaccion.TipoEnum.TRANSFERENCIA)
                        .monto(transferencia.getMonto())
                        .cuentaOrigen(transferencia.getCuentaOrigen())
                        .cuentaDestino(transferencia.getCuentaDestino())
                        .fecha(OffsetDateTime.now());
                transaccionRepository.save(transaccion);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(responseAcreditar.getStatusCode()).build();
            }
        } else {
            return ResponseEntity.status(responseDebitar.getStatusCode()).build();
        }
    }


    public List<HistorialTransaccion> obtenerHistorialTransacciones() {
        return transaccionRepository.findAll();
    }
}
