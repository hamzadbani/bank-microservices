package com.microservices.loans.exception;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String... messages) {
        super(messages);
    }
}
