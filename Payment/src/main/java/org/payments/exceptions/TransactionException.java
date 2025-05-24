package org.payments.exceptions;

import lombok.Getter;

@Getter
public class TransactionException extends RuntimeException {
    private final int status;

    public TransactionException(String message, int status){
        super(message);
        this.status = status;
    }
}
