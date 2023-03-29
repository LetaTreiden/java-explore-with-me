package ru.practicum.explorewithme.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(final IllegalArgumentException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, "Something wrong with argument",
                e.getMessage(), LocalDateTime.now());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleViolationException(final DataIntegrityViolationException e) {
        return new ErrorResponse(HttpStatus.CONFLICT,
                "Constantly violation exception",
                e.getMessage(),
                LocalDateTime.now());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSuchElementException(final NoSuchElementException e) {
        return new ErrorResponse(HttpStatus.NOT_FOUND, "Element not found", e.getMessage(), LocalDateTime.now());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalStateException(final IllegalStateException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request",
                e.getMessage(), LocalDateTime.now());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleValidationException(final ValidationException e) {
        return new ErrorResponse(HttpStatus.CONFLICT,
                "Something wrong with argument",
                e.getMessage(),
                LocalDateTime.now());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleArgumentNotValidException(final MethodArgumentNotValidException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST,
                "Wrong operation",
                e.getMessage(),
                LocalDateTime.now());
    }
}
