package com.nttdata.TransactionMs.api;

import com.nttdata.TransactionMs.model.HistorialTransaccion;
import com.nttdata.TransactionMs.model.TransaccionDeposito;
import com.nttdata.TransactionMs.model.TransaccionRetiro;
import com.nttdata.TransactionMs.model.TransaccionTransferencia;
import com.nttdata.TransactionMs.service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransaccionesApiDelegateImpl implements TransaccionesApiDelegate {

    private final TransaccionService transaccionService;

    @Autowired
    public TransaccionesApiDelegateImpl(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @Override
    public ResponseEntity<List<HistorialTransaccion>> historialTransacciones() {
        List<HistorialTransaccion> historial = transaccionService.obtenerHistorialTransacciones();
        return ResponseEntity.ok(historial);
    }


    @Override
    public ResponseEntity<Void> registrarDeposito(TransaccionDeposito transaccionDeposito) {
        return transaccionService.registrarDeposito(transaccionDeposito);
    }

    @Override
    public ResponseEntity<Void> registrarRetiro(TransaccionRetiro transaccionRetiro) {
        return transaccionService.registrarRetiro(transaccionRetiro);
    }

    @Override
    public ResponseEntity<Void> registrarTransferencia(TransaccionTransferencia transaccionTransferencia) {
        return transaccionService.registrarTransferencia(transaccionTransferencia);
    }
}












