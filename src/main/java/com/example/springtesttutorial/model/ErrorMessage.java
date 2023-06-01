package com.example.springtesttutorial.model;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorMessage {
  
  private String status;
  private String exception;
  private String message;
  private Date date;
}
