package com.eteration.simplebanking.model;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException() {
        super("Insufficient balance in the account");
    }
}
 