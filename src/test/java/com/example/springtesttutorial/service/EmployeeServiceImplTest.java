package com.example.springtesttutorial.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.springtesttutorial.exception.EmployeeAlreadyExistsException;
import com.example.springtesttutorial.exception.EmployeeNotFoundException;
import com.example.springtesttutorial.model.Employee;
import com.example.springtesttutorial.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

  @Mock
  private EmployeeRepository employeeRepository;

  @InjectMocks
  private EmployeeServiceImpl employeeService;

  private Employee employee;

  @BeforeEach
  void setUp() {
    // employeeRepository = Mockito.mock(EmployeeRepository.class);
    // employeeService = new EmployeeServiceImpl(employeeRepository);

    employee = Employee.builder().firstName("John").lastName("Doe").email("John@mail.com").build();
  }

  @Test
  @DisplayName("save employee")
  public void givenEmployee_whenSaveEmployee_thenEmployeeIsSaved() {
    // given
    BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
    BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

    // when
    Employee savedEmployee = employeeService.saveEmployee(employee);

    // then
    assertThat(savedEmployee).isNotNull();
  }

  @Test
  @DisplayName("save employee exception")
  public void givenExistingEmail_whenSaveEmployee_thenThrowException() {
    // given
    BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

    // when
    assertThrows(EmployeeAlreadyExistsException.class, () -> {
      employeeService.saveEmployee(employee);
    });

    // then
    verify(employeeRepository, never()).save(employee);
  }

  @Test
  @DisplayName("get all employees")
  public void givenEmployees_whenGetAllEmployees_thenReturnEmployees() {
    // given
    Employee employee2 = Employee.builder().firstName("John").lastName("Doe").email("John@mail.com").build();
    BDDMockito.given(employeeRepository.findAll()).willReturn(List.of(employee, employee2));

    // when
    List<Employee> employees = employeeService.getAllEmployees();

    // then
    assertThat(employees).isNotNull();
    assertThat(employees).hasSize(2);
    assertThat(employees.size()).isEqualTo(2);
  }

  @Test
  @DisplayName("get all employees empty")
  public void givenNoEmployees_whenGetAllEmployees_thenReturnEmptyList() {
    // given
    BDDMockito.given(employeeRepository.findAll()).willReturn(List.of());

    // when
    List<Employee> employees = employeeService.getAllEmployees();

    // then
    assertThat(employees).isNotNull();
    assertThat(employees).hasSize(0);
    assertThat(employees.size()).isEqualTo(0);
  }

  @Test
  @DisplayName("get employee by id")
  public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() {
    // given
    BDDMockito.given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));

    // when
    Employee foundEmployee = employeeService.getEmployeeById(employee.getId());

    // then
    assertThat(foundEmployee).isNotNull();
    assertThat(foundEmployee.getEmail()).isEqualTo(employee.getEmail());
  }

  @Test
  @DisplayName("get employee by id exception")
  public void givenNoEmployeeId_whenGetEmployeeById_thenThrowException() {
    // given
    BDDMockito.given(employeeRepository.findById(50L)).willReturn(Optional.empty());

    // when
    assertThrows(EmployeeNotFoundException.class, () -> {
      employeeService.getEmployeeById(50L);
    });
  }

  @Test
  @DisplayName("update employee")
  public void givenEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() {
    // given
    Employee employee = Employee.builder().id(1L).firstName("John").lastName("Doe").email("John@mail.com").build();
    Employee updatedEmployee = Employee.builder().id(1L).firstName("Jack").lastName("Doe").email("John@mail.com")
        .build();

    BDDMockito.given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
    BDDMockito.given(employeeRepository.save(updatedEmployee)).willReturn(updatedEmployee);

    // when
    Employee result = employeeService.updateEmployee(1L, updatedEmployee);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getFirstName()).isEqualTo(updatedEmployee.getFirstName());
    verify(employeeRepository).findById(employee.getId());
    verify(employeeRepository).save(updatedEmployee);
  }

  @Test
  @DisplayName("update employee exception")
  public void givenNoEmployee_whenUpdateEmployee_thenThrowException() {
    // given
    Long id = 99L;
    Employee updatedEmployee = Employee.builder().id(id).firstName("Jack").lastName("Doe").email("John@mail.com")
        .build();

    BDDMockito.given(employeeRepository.findById(updatedEmployee.getId())).willReturn(Optional.empty());

    // when
    assertThrows(EmployeeNotFoundException.class, () -> {
      employeeService.updateEmployee(id,updatedEmployee);
    });

    // then
    verify(employeeRepository).findById(updatedEmployee.getId());
    verify(employeeRepository, never()).save(updatedEmployee);
  }

  @Test
  @DisplayName("delete employee")
  public void givenEmployeeId_whenDeleteEmployee_thenEmployeeIsDeleted() {
    // given
    BDDMockito.doNothing().when(employeeRepository).deleteById(employee.getId());

    // when
    employeeService.deleteEmployee(employee.getId());

    // then
    verify(employeeRepository).deleteById(employee.getId());
  }
  

}
