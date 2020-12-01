package com.antonina.springapp.exceptions;

public class IllegalUsernameException extends Exception {
    public IllegalUsernameException() {
        super("Nazwa użytkownika zajęta.");
    }
}