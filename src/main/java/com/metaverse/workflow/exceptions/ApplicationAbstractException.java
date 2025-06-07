package com.metaverse.workflow.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public abstract class ApplicationAbstractException extends Exception {

    private final Object errorCode;
    public List<String> errors;
    private int statusCode;

    public ApplicationAbstractException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ApplicationAbstractException(String message, String errorCode, int statusCode) {
        this(message, errorCode);
        this.statusCode = statusCode;
    }

    public ApplicationAbstractException(String message, String errorCode, int statusCode, List<String> errors) {
        this(message, errorCode, statusCode);
        this.errors = errors;
    }
}