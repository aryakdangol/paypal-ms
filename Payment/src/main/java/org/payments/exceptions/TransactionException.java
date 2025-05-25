package org.payments.exceptions;

import lombok.Getter;

@Getter
public class TransactionException extends RuntimeException {
    private final int status;
    private final String method;

    public TransactionException(String message, int status, String method){
        super(message);
        this.status = status;
        this.method = method;
    }
}
