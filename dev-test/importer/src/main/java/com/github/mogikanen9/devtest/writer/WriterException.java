package com.github.mogikanen9.devtest.writer;

public class WriterException extends Exception {

    private static final long serialVersionUID = 1L;

    public WriterException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public WriterException(String message) {
        super(message);
    }
}