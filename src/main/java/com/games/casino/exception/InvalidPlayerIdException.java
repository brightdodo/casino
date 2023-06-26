package com.games.casino.exception;

public class InvalidPlayerIdException extends RuntimeException{
    public InvalidPlayerIdException(String message) {
        super(message);
    }
}
