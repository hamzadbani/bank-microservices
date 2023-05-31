package com.microservices.accounts.exception;

public class AccountsException extends BaseException {
    public AccountsException(String... messages) {
        super(messages);
    }
}
