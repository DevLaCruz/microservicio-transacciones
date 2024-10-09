package com.nttdata.TransactionMs.repository;

import com.nttdata.TransactionMs.model.HistorialTransaccion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaccionRepository extends MongoRepository<HistorialTransaccion, String> {

}
