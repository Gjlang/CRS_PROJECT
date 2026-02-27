package com.crs.ejb.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) { super(message); }
}