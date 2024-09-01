package com.bank.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_GATEWAY)
public class CustomerAlreadyExistExpection extends RuntimeException{

    public CustomerAlreadyExistExpection(String message) {
        super(message);
    }
}
