package com.mycompany.education.exceptions;

public class CadastroException extends Exception {

    public CadastroException(String message) {
        super(message);
    }

    public CadastroException(String message, Throwable cause) {
        super(message, cause);
    }
}