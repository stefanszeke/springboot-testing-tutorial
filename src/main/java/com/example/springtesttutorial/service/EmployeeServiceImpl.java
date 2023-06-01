package com.example.springtesttutorial.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.example.springtesttutorial.exception.EmployeeAlreadyExistsException;
import com.example.springtesttutorial.exception.EmployeeNotFoundException;
import com.example.springtesttutorial.model.Employee;
import com.example.springtesttutorial.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;

  public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  @Override
  public Employee saveEmployee(Employee employee) {

    employeeRepository.findByEmail(employee.getEmail())
        .ifPresent(e -> {
          throw new EmployeeAlreadyExistsException(String.format("Employee with email %s already exists", employee.getEmail()));
        });

    return employeeRepository.save(employee);
  }

  @Override
  public List<Employee> getAllEmployees() {
    return employeeRepository.findAll();
  }

  @Override
  public Employee getEmployeeById(Long id) {
    return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(String.format("Employee not found for id %s", id)));
  }

  @Override
  public Employee updateEmployee(Long id, Employee updatedEmployee) {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new EmployeeNotFoundException(String.format("Employee not found for id %s", id)));

    if(updatedEmployee.getFirstName() != null) employee.setFirstName(updatedEmployee.getFirstName());
    if(updatedEmployee.getLastName() != null) employee.setLastName(updatedEmployee.getLastName());
    if(updatedEmployee.getEmail() != null) employee.setEmail(updatedEmployee.getEmail());

    return employeeRepository.save(employee);
  }

  @Override
  public void deleteEmployee(Long id) {
    employeeRepository.deleteById(id);
  }
  
}
