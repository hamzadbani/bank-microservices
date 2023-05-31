package com.microservices.cards.exception;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String... messages) {
        super(messages);
    }
}
