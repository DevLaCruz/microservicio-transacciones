package com.nttdata.TransactionMs.controller;

import com.nttdata.TransactionMs.api.TransaccionesApiDelegate;
import com.nttdata.TransactionMs.model.TransaccionDeposito;
import com.nttdata.TransactionMs.model.TransaccionRetiro;
import com.nttdata.TransactionMs.model.TransaccionTransferencia;
import com.nttdata.TransactionMs.model.HistorialTransaccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacciones")
public class TransaccionControllerImpl {

    private final TransaccionesApiDelegate transaccionesApiDelegate;

    @Autowired
    public TransaccionControllerImpl(TransaccionesApiDelegate transaccionesApiDelegate) {
        this.transaccionesApiDelegate = transaccionesApiDelegate;
    }

    @PostMapping("/deposito")
    public ResponseEntity<Void> registrarDeposito(@RequestBody TransaccionDeposito transaccionDeposito) {
        return transaccionesApiDelegate.registrarDeposito(transaccionDeposito);
    }

    @PostMapping("/retiro")
    public ResponseEntity<Void> registrarRetiro(@RequestBody TransaccionRetiro transaccionRetiro) {
        return transaccionesApiDelegate.registrarRetiro(transaccionRetiro);
    }

    @PostMapping("/transferencia")
    public ResponseEntity<Void> registrarTransferencia(@RequestBody TransaccionTransferencia transaccionTransferencia) {
        return transaccionesApiDelegate.registrarTransferencia(transaccionTransferencia);
    }

    @GetMapping("/historial")
    public ResponseEntity<List<HistorialTransaccion>> obtenerHistorialTransacciones() {
        return transaccionesApiDelegate.historialTransacciones();
    }
}
