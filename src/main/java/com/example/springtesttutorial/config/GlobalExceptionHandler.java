package com.example.springtesttutorial.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.springtesttutorial.exception.EmployeeAlreadyExistsException;
import com.example.springtesttutorial.exception.EmployeeNotFoundException;
import com.example.springtesttutorial.model.ErrorMessage;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EmployeeAlreadyExistsException.class)
  public ResponseEntity<ErrorMessage> handleEmployeeAlreadyExistsException(EmployeeAlreadyExistsException exception) {

    HttpStatus status = HttpStatus.CONFLICT;

    ErrorMessage errorMessage = new ErrorMessage();
    errorMessage.setStatus(status.toString());
    errorMessage.setException(exception.getClass().getSimpleName());
    errorMessage.setMessage(exception.getMessage());
    errorMessage.setDate(new java.util.Date());

    return new ResponseEntity<>(errorMessage, status);
  }

  @ExceptionHandler(EmployeeNotFoundException.class)
  public ResponseEntity<ErrorMessage> handleEmployeeNotFoundException(EmployeeNotFoundException exception) {

    HttpStatus status = HttpStatus.NOT_FOUND;

    ErrorMessage errorMessage = new ErrorMessage();
    errorMessage.setStatus(status.toString());
    errorMessage.setException(exception.getClass().getSimpleName());
    errorMessage.setMessage(exception.getMessage());
    errorMessage.setDate(new java.util.Date());

    return new ResponseEntity<>(errorMessage, status);
  }
  
}
