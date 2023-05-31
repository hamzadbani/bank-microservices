package com.microservices.accounts.exception;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String... messages) {
            super(messages);
    }
}
