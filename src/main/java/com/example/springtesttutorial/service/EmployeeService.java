package com.example.springtesttutorial.service;


import java.util.List;

import com.example.springtesttutorial.model.Employee;

public interface EmployeeService {
  
  Employee saveEmployee(Employee employee);

  List<Employee> getAllEmployees();

  Employee getEmployeeById(Long id);

  Employee updateEmployee(Long id, Employee updatedEmployee);

  void deleteEmployee(Long id);
}
