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
import java.util.List;
import java.util.Objects;

@Service
public class TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private RestTemplate restTemplate;  // Inyectamos RestTemplate

    private static final String CUENTAS_MS_BASE_URL = "http://localhost:8081/cuentas";  // Cambia la URL según tu configuración


    public ResponseEntity<Void> registrarDeposito(TransaccionDeposito deposito) {
        // URL para depositar fondos
        String url = CUENTAS_MS_BASE_URL + "/" + deposito.getCuentaId() + "/depositar";

        // Realizamos la solicitud usando PUT
        HttpEntity<TransaccionDeposito> requestEntity = new HttpEntity<>(deposito);
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            // Registrar transacción si el depósito fue exitoso
            HistorialTransaccion transaccion = new HistorialTransaccion()
                    .tipo(HistorialTransaccion.TipoEnum.DEPOSITO)
                    .monto(deposito.getMonto())
                    .cuentaDestinoId(deposito.getCuentaId())
                    .fecha(OffsetDateTime.now());
            transaccionRepository.save(transaccion);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }


    public ResponseEntity<Void> registrarRetiro(TransaccionRetiro retiro) {
        String url = CUENTAS_MS_BASE_URL + "/" + retiro.getCuentaId() + "/retirar";
        HttpEntity<TransaccionRetiro> requestEntity = new HttpEntity<>(retiro);

        try {
            // Realiza la solicitud
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                // Registrar la transacción si el retiro fue exitoso
                HistorialTransaccion transaccion = new HistorialTransaccion()
                        .tipo(HistorialTransaccion.TipoEnum.RETIRO)
                        .monto(retiro.getMonto())
                        .cuentaOrigenId(retiro.getCuentaId())
                        .fecha(OffsetDateTime.now());
                transaccionRepository.save(transaccion);
                return ResponseEntity.ok().build();
            }

        } catch (HttpClientErrorException e) {
            // Captura la excepción 400 y lanza la excepción personalizada
            String errorMessage = e.getResponseBodyAsString();
            throw new TransaccionException(errorMessage, HttpStatus.BAD_REQUEST);
        }

        // En caso de que no haya sido exitoso
        throw new TransaccionException("Error desconocido al procesar el retiro", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    public ResponseEntity<Void> registrarTransferencia(TransaccionTransferencia transferencia) {
        // URL para debitar de la cuenta origen
        String urlDebitar = CUENTAS_MS_BASE_URL + "/" + transferencia.getCuentaOrigenId() + "/retirar";
        HttpEntity<TransaccionTransferencia> requestEntityDebitar = new HttpEntity<>(transferencia);
        ResponseEntity<Void> responseDebitar = restTemplate.exchange(urlDebitar, HttpMethod.PUT, requestEntityDebitar, Void.class);

        if (responseDebitar.getStatusCode().is2xxSuccessful()) {
            // URL para acreditar en la cuenta destino
            String urlAcreditar = CUENTAS_MS_BASE_URL + "/" + transferencia.getCuentaDestinoId() + "/depositar";
            HttpEntity<TransaccionTransferencia> requestEntityAcreditar = new HttpEntity<>(transferencia);
            ResponseEntity<Void> responseAcreditar = restTemplate.exchange(urlAcreditar, HttpMethod.PUT, requestEntityAcreditar, Void.class);

            if (responseAcreditar.getStatusCode().is2xxSuccessful()) {
                // Registrar la transferencia si ambos pasos fueron exitosos
                HistorialTransaccion transaccion = new HistorialTransaccion()
                        .tipo(HistorialTransaccion.TipoEnum.TRANSFERENCIA)
                        .monto(transferencia.getMonto())
                        .cuentaOrigenId(transferencia.getCuentaOrigenId())
                        .cuentaDestinoId(transferencia.getCuentaDestinoId())
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
