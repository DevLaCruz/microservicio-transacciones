package com.nttdata.TransactionMs.service;

import com.nttdata.TransactionMs.model.HistorialTransaccion;
import com.nttdata.TransactionMs.model.TransaccionDeposito;
import com.nttdata.TransactionMs.model.TransaccionRetiro;
import com.nttdata.TransactionMs.model.TransaccionTransferencia;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransaccionService {

    // Simulación de un historial de transacciones
    private List<HistorialTransaccion> historial = new ArrayList<>();

    public ResponseEntity<Void> registrarDeposito(TransaccionDeposito deposito) {
        // Lógica para registrar un depósito
        // Asegúrate de validar la transacción y actualizar el historial
        HistorialTransaccion transaccion = new HistorialTransaccion();
        transaccion.setTipo(HistorialTransaccion.TipoEnum.valueOf("DEPOSITO"));
        transaccion.setMonto(deposito.getMonto());
        // Agregar más campos necesarios al historial...
        historial.add(transaccion);

        return ResponseEntity.ok().build(); // Devuelve 200 OK
    }

    public ResponseEntity<Void> registrarRetiro(TransaccionRetiro retiro) {
        // Lógica para registrar un retiro
        // Asegúrate de validar la transacción y actualizar el historial
        HistorialTransaccion transaccion = new HistorialTransaccion();
        transaccion.setTipo(HistorialTransaccion.TipoEnum.valueOf("RETIRO"));
        transaccion.setMonto(retiro.getMonto());
        // Agregar más campos necesarios al historial...
        historial.add(transaccion);

        return ResponseEntity.ok().build(); // Devuelve 200 OK
    }

    public ResponseEntity<Void> registrarTransferencia(TransaccionTransferencia transferencia) {
        // Lógica para registrar una transferencia
        // Asegúrate de validar la transacción y actualizar el historial
        HistorialTransaccion transaccion = new HistorialTransaccion();
        transaccion.setTipo(HistorialTransaccion.TipoEnum.valueOf("TRANSFERENCIA"));
        transaccion.setMonto(transferencia.getMonto());
        // Agregar más campos necesarios al historial...
        historial.add(transaccion);

        return ResponseEntity.ok().build(); // Devuelve 200 OK
    }

    public List<HistorialTransaccion> obtenerHistorialTransacciones() {
        return historial; // Devuelve el historial de transacciones
    }
}
