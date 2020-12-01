package com.antonina.springapp.exceptions;

public class NoSuchUserException extends Exception {
    public NoSuchUserException() {
        super("Nie ma takiego użytkownika.");
    }
}
