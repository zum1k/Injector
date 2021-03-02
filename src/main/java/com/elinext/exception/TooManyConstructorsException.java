package com.elinext.exception;

public class TooManyConstructorsException extends RuntimeException {
  private String message;

  public TooManyConstructorsException(String message) {
    super(message);
  }
}
