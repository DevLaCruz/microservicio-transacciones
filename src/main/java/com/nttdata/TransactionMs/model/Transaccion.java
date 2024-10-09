package com.nttdata.TransactionMs.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "transacciones")
public class Transaccion {

    @Id
    private Integer cuentaId;
    private Integer cuentaOrigenId;
    private Integer cuentaDestinoId;
    private double monto;
    private String tipo;  // DEPOSITO, RETIRO, TRANSFERENCIA
    private String fecha;
}
