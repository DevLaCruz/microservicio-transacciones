package com.nttdata.TransactionMs.exception;

import org.springframework.http.HttpStatus;

public class TransaccionException extends RuntimeException {

    private final HttpStatus status;

    public TransaccionException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
