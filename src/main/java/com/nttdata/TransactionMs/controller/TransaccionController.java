package com.nttdata.TransactionMs.api;

import com.nttdata.TransactionMs.model.Transaccion;
import com.nttdata.TransactionMs.service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacciones")
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;

    @PostMapping("/deposito")
    public ResponseEntity<Transaccion> registrarDeposito(@RequestBody Transaccion transaccion) {
        Transaccion resultado = transaccionService.registrarDeposito(transaccion);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/retiro")
    public ResponseEntity<?> registrarRetiro(@RequestBody Transaccion transaccion) {
        try {
            Transaccion resultado = transaccionService.registrarRetiro(transaccion);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/transferencia")
    public ResponseEntity<?> registrarTransferencia(@RequestBody Transaccion transaccion) {
        try {
            Transaccion resultado = transaccionService.registrarTransferencia(transaccion);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/historial")
    public ResponseEntity<List<Transaccion>> obtenerHistorial() {
        List<Transaccion> historial = transaccionService.obtenerHistorial();
        return ResponseEntity.ok(historial);
    }
}
