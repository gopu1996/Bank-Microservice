package com.bank.loans.exception;

public class LoansAlreadyExistsException extends RuntimeException {

    public LoansAlreadyExistsException(String msg){
        super(msg);
    }
}
