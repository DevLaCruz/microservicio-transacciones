package com.nttdata.TransactionMs.repository;

import com.nttdata.TransactionMs.model.Transaccion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransaccionRepository extends MongoRepository<Transaccion, Integer> {
    // Aquí puedes agregar métodos personalizados si es necesario
}
