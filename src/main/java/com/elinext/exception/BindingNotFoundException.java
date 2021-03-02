package com.elinext.exception;

public class BindingNotFoundException extends RuntimeException {
  private String message;
  public BindingNotFoundException(String message){
    super(message);
  }
}
