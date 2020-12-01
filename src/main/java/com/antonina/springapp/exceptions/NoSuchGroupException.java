package com.antonina.springapp.exceptions;

public class NoSuchGroupException extends Exception {
    public NoSuchGroupException() {
        super("Nie ma takiej grupy.");
    }
}
