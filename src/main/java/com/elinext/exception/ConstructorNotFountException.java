package com.elinext.exception;

public class ConstructorNotFountException  extends RuntimeException{
  private String message;
  public ConstructorNotFountException(String message){
    super(message);
  }
}
