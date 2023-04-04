package ru.practicum.explorewithme.commonhandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleIllegalArgumentException(final IllegalArgumentException e) {
    return new ErrorResponse(HttpStatus.BAD_REQUEST, "Incorrectly made request.", e.getMessage(), LocalDateTime.now());
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleNoSuchElementException(final NoSuchElementException e) {
    return new ErrorResponse(HttpStatus.NOT_FOUND, "Entity not found.", e.getMessage(), LocalDateTime.now());
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleIllegalStateException(final IllegalStateException e) {
    return new ErrorResponse(HttpStatus.BAD_REQUEST, "Incorrectly made request.", e.getMessage(), LocalDateTime.now());
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorResponse handleConstraintViolationException(final DataIntegrityViolationException e) {
    return new ErrorResponse(HttpStatus.CONFLICT,
        "Integrity constraint has been violated.",
        e.getMessage(),
        LocalDateTime.now());
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
    return new ErrorResponse(HttpStatus.BAD_REQUEST,
        "For the requested operation the conditions are not met.",
        e.getMessage(),
        LocalDateTime.now());
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorResponse handleValidationException(final ValidationException e) {
    return new ErrorResponse(HttpStatus.CONFLICT,
        "For the requested operation the conditions are not met.",
        e.getMessage(),
        LocalDateTime.now());
  }
}
