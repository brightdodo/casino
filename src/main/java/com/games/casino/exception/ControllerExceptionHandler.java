package com.games.casino.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({InvalidPlayerIdException.class, InvalidAmountException.class, IllegalArgumentException.class})
    ErrorResponse handleInvalidIdOrAmountException(Exception exception){
        return ErrorResponse.builder(exception, HttpStatus.BAD_REQUEST, exception.getMessage())
                .title("Player Not Found")
                .type(URI.create("https://games.com/errors/not-found"))
                .build();
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    ErrorResponse handleInsufficientBalanceException(InsufficientBalanceException exception){
        return ErrorResponse.builder(exception, HttpStatus.I_AM_A_TEAPOT, exception.getMessage())
                .title("Insufficient Balance")
                .type(URI.create("https://games.com/errors/insufficient-balance"))
                .build();
    }
}
