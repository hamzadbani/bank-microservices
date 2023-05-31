package com.microservices.loans.exception;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class BaseException extends RuntimeException {
    private String[] messages;

    public BaseException(Throwable cause, String... messages) {
        super(Arrays.toString(messages), cause);
    }

    public BaseException(String... messages) {
        super(StringUtils.substring(Arrays.toString(messages), 1, Arrays.toString(messages).length() - 1));
        this.messages = messages;
    }

    public String[] getMessageArray() {
        return messages;
    }
}
