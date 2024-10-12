package com.nttdata.TransactionMs.dto;

import com.nttdata.TransactionMs.model.HistorialTransaccion;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;

@Document(collection = "historialTransaccion") // Indica que esta clase representa una colección en MongoDB
public class HistorialTransaccionDTO extends HistorialTransaccion {

    public HistorialTransaccionDTO() {
        super(); // Llama al constructor de la clase base
    }

    public HistorialTransaccionDTO(TipoEnum tipo, Double monto, OffsetDateTime fecha, Integer cuentaOrigenId, Integer cuentaDestinoId) {
        super();
        setTipo(tipo);
        setMonto(monto);
        setFecha(fecha);
        setCuentaOrigenId(cuentaOrigenId);
        setCuentaDestinoId(cuentaDestinoId);
    }

    @Override
    public String toString() {
        return "HistorialTransaccionDTO{" +
                "tipo=" + getTipo() +
                ", monto=" + getMonto() +
                ", fecha=" + getFecha() +
                ", cuentaOrigenId=" + getCuentaOrigenId() +
                ", cuentaDestinoId=" + getCuentaDestinoId() +
                '}';
    }

    // Método para convertir la fecha a un formato específico si es necesario
    public String getFechaFormatted() {
        return getFecha() != null ? getFecha().toString() : null; // Aquí puedes cambiar el formato según lo necesites
    }
}
