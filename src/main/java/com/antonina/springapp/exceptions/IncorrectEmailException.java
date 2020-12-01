package com.antonina.springapp.exceptions;

public class IncorrectEmailException extends Exception {
    public IncorrectEmailException() {
        super("Nieprawidłowy adres e-mail.");
    }
}
