package com.nttdata.TransactionMs.service;

import com.nttdata.TransactionMs.model.HistorialTransaccion;
import com.nttdata.TransactionMs.model.TransaccionDeposito;
import com.nttdata.TransactionMs.repository.TransaccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TransaccionServiceTest {

    @InjectMocks
    private TransaccionService transaccionService;

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private RestTemplate restTemplate;

    private TransaccionDeposito deposito;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deposito = new TransaccionDeposito();
        deposito.setCuentaId(1);
        deposito.setMonto(100.0);
    }

    @Test
    void testRegistrarDeposito() {
        when(restTemplate.exchange(anyString(), any(), any(), eq(Void.class))).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Void> response = transaccionService.registrarDeposito(deposito);

        assertEquals(200, response.getStatusCodeValue());
        verify(transaccionRepository, times(1)).save(any(HistorialTransaccion.class));
    }
}
