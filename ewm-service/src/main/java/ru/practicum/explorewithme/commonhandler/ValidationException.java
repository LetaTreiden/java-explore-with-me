package ru.practicum.explorewithme.commonhandler;

public class ValidationException extends IllegalArgumentException {

  public ValidationException(String message) {
    super(message);
  }
}
