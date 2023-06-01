package com.example.springtesttutorial.exception;

public class EmployeeAlreadyExistsException extends RuntimeException {

  public EmployeeAlreadyExistsException(String message) {
    super(message);
  }

  public EmployeeAlreadyExistsException(String message, Throwable cause) {
    super(message, cause);
  }
  
}
