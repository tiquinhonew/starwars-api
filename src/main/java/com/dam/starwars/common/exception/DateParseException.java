package com.dam.starwars.common.exception;

public class DateParseException extends RuntimeException {

    public DateParseException(String message) {
        super(message);
    }

    public DateParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
