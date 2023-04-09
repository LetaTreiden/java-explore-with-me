package ru.practicum.explorewithme.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class ExceptionsHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(final IllegalArgumentException e) {
        log.info("smth wrong with argument, check and try again, \n {}", (Object[]) e.getStackTrace());
        return new ErrorResponse(HttpStatus.BAD_REQUEST, "Something wrong with argument",
                e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleViolationException(final DataIntegrityViolationException e) {
        log.info("Constantly violation exception\n{}", (Object[]) e.getStackTrace());
        return new ErrorResponse(HttpStatus.CONFLICT,
                "Constantly violation exception",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSuchElementException(final NoSuchElementException e) {
        log.info("There is no such element. Check and try again\n{}", (Object[]) e.getStackTrace());
        return new ErrorResponse(HttpStatus.NOT_FOUND, "Element not found", e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalStateException(final IllegalStateException e) {
        log.info("smth wrong, check your request and try again\n{}", (Object[]) e.getStackTrace());
        return new ErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request",
                e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.info("smth wrong with argument!, check and try again\n{}", (Object[]) e.getStackTrace());
        return new ErrorResponse(HttpStatus.CONFLICT,
                "Something wrong with argument",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.info("smth wrong with operation, check and try again\n{}", (Object[]) e.getStackTrace());
        return new ErrorResponse(HttpStatus.BAD_REQUEST,
                "Wrong operation",
                e.getMessage(),
                LocalDateTime.now());
    }
}
