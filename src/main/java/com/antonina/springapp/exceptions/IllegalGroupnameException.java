package com.antonina.springapp.exceptions;

public class IllegalGroupnameException extends Exception {
    public IllegalGroupnameException() {
        super("Nazwa grupy jest już zajęta.");
    }
}
