package com.elinext.exception;

public class BeanClassesNotFoundException extends RuntimeException {
  private String message;

  public BeanClassesNotFoundException(String message) {
    super(message);
  }
}
