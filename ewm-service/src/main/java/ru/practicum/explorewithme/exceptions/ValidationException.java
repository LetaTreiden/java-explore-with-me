package ru.practicum.explorewithme.exceptions;

public class ValidationException extends IllegalArgumentException {

  public ValidationException(String message) {
    super(message);
  }
}
