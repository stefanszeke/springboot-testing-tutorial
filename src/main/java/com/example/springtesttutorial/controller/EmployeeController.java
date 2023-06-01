package com.example.springtesttutorial.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.springtesttutorial.model.Employee;
import com.example.springtesttutorial.service.EmployeeService;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {
 
  private EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Employee saveEmployee(@RequestBody Employee employee) {
    return employeeService.saveEmployee(employee);
  }

  @GetMapping
  public List<Employee> getAllEmployees() {
    return employeeService.getAllEmployees();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
    Employee employee = employeeService.getEmployeeById(id);
    return new ResponseEntity<>(employee, HttpStatus.OK);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
    Employee employee = employeeService.updateEmployee(id, updatedEmployee);
    return new ResponseEntity<>(employee, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
    employeeService.deleteEmployee(id);
    return new ResponseEntity<>("Employee deleted successfully", HttpStatus.OK);
  }
}
