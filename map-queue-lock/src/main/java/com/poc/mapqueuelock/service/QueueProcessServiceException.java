package com.poc.mapqueuelock.service;

public class QueueProcessServiceException extends Exception{

    private static final long serialVersionUID = 1L;

    public QueueProcessServiceException(Throwable cause) {
        super(cause);
    }

    public QueueProcessServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}