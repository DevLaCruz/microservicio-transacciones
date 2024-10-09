package com.nttdata.TransactionMs.service;

import com.nttdata.TransactionMs.model.Transaccion;
import com.nttdata.TransactionMs.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    public Transaccion registrarDeposito(Transaccion transaccion) {
        transaccion.setTipo("DEPOSITO");
        return transaccionRepository.save(transaccion);
    }

    public Transaccion registrarRetiro(Transaccion transaccion) throws Exception {
        Optional<Transaccion> cuenta = transaccionRepository.findById(transaccion.getCuentaId());
        if (cuenta.isPresent() && cuenta.get().getMonto() >= transaccion.getMonto()) {
            transaccion.setTipo("RETIRO");
            return transaccionRepository.save(transaccion);
        } else {
            throw new Exception("Fondos insuficientes o cuenta no encontrada.");
        }
    }

    public Transaccion registrarTransferencia(Transaccion transaccion) throws Exception {
        Optional<Transaccion> cuentaOrigen = transaccionRepository.findById(transaccion.getCuentaOrigenId());
        Optional<Transaccion> cuentaDestino = transaccionRepository.findById(transaccion.getCuentaDestinoId());
        if (cuentaOrigen.isPresent() && cuentaDestino.isPresent() && cuentaOrigen.get().getMonto() >= transaccion.getMonto()) {
            transaccion.setTipo("TRANSFERENCIA");
            // Actualizar saldos
            cuentaOrigen.get().setMonto(cuentaOrigen.get().getMonto() - transaccion.getMonto());
            cuentaDestino.get().setMonto(cuentaDestino.get().getMonto() + transaccion.getMonto());
            transaccionRepository.save(cuentaOrigen.get());
            transaccionRepository.save(cuentaDestino.get());
            return transaccionRepository.save(transaccion);
        } else {
            throw new Exception("Fondos insuficientes o cuenta no encontrada.");
        }
    }

    public List<Transaccion> obtenerHistorial() {
        return transaccionRepository.findAll();
    }
}
