package com.bank.cards.exception;

public class CardAlreadyExistsException extends RuntimeException {

    public CardAlreadyExistsException(String msg){
        super(msg);
    }
}
