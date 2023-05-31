package com.example.springtesttutorial.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.springtesttutorial.model.Employee;

@DataJpaTest // auto-configures an in-memory database, Spring Data JPA, and Hibernate, and
             // scans for @Entity classes and Spring Data repositories, disables full
             // auto-configuration and applies only configuration relevant to JPA tests
public class EmployeeRepositoryTest {

  @Autowired
  private EmployeeRepository employeeRepository;

  private Employee employee;

  @BeforeEach
  public void setUp() {
    employee = Employee.builder()
        .firstName("John")
        .lastName("Doe")
        .email("John@gmail.com")
        .build();
  }

  // JUnit test for find by first name and last name (Custom JPQL query)
  @Test
  @DisplayName("Find by first name and last name (Custom JPQL query)")
  public void givenFirstAndLastName_whenFindByJPQL_thenReturnEmployee() {
    // given
    employeeRepository.save(employee);

    String firstName = "John";
    String lastName = "Doe";

    // when
    Employee foundEmployee = employeeRepository.findByJPQL(firstName, lastName);

    // then
    assertThat(foundEmployee).isNotNull();
    assertThat(foundEmployee.getFirstName()).isEqualTo(firstName);
  }

  // JUnit test for find by first name and last name (Custom JPQL query) named parameters
  @Test
  @DisplayName("Find by first name and last name (Custom JPQL query) named parameters")
  public void givenFirstAndLastName_whenFindByJPQLNamedParameters_thenReturnEmployee() {
    // given
    employeeRepository.save(employee);

    String firstName = "John";
    String lastName = "Doe";

    // when
    Employee foundEmployee = employeeRepository.findByJPQL(firstName, lastName);

    // then
    assertThat(foundEmployee).isNotNull();
    assertThat(foundEmployee.getFirstName()).isEqualTo(firstName);
  }

  // JUnit test for save employee
  @Test
  @DisplayName("Save employee")
  public void givenEmployee_whenSave_thenReturnSavedEmployee() {
    // given

    // when
    Employee savedEmployee = employeeRepository.save(employee);

    // then
    assertThat(savedEmployee).isNotNull();
    assertThat(savedEmployee.getId()).isGreaterThan(0);
  }

  // JUnit test for get all employees
  @Test
  @DisplayName("Get all employees")
  public void givenEmployees_whenFindAll_thenReturnAllEmployees() {
    // given
    Employee employee1 = Employee.builder()
        .firstName("John")
        .lastName("Doe")
        .email("Jonh@mail.com")
        .build();

    Employee employee2 = Employee.builder()
        .firstName("Jane")
        .lastName("Doe")
        .email("Jane@gmail.com")
        .build();

    employeeRepository.save(employee1);
    employeeRepository.save(employee2);

    // when
    List<Employee> employees = employeeRepository.findAll();

    // then
    assertThat(employees).isNotNull();
    assertThat(employees.size()).isEqualTo(2);
  }

  // JUnit test for get employee by id
  @Test
  @DisplayName("Get employee by id")
  public void givenEmployeeId_whenFindById_thenReturnEmployee() {
    // given
    Employee savedEmployee = employeeRepository.save(employee);

    // when
    Employee foundEmployee = employeeRepository.findById(savedEmployee.getId()).orElse(null);

    // then
    assertThat(foundEmployee).isNotNull();
  }

  // JUnit test for find by email
  @Test
  @DisplayName("Find by email")
  public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployee() {
    // given
    employeeRepository.save(employee);

    // when
    Employee foundEmployee = employeeRepository.findByEmail(employee.getEmail()).orElse(null);

    // then
    assertThat(foundEmployee).isNotNull();
  }

  // JUnit test for update employee
  @Test
  @DisplayName("Update employee")
  public void givenEmployee_whenUpdate_thenReturnUpdatedEmployee() {
    // given
    employeeRepository.save(employee);

    // when
    Employee savedEmployee = employeeRepository.findById(employee.getId()).orElse(null);
    savedEmployee.setFirstName("Jane");

    Employee updatedEmployee = employeeRepository.save(savedEmployee);

    // then
    assertThat(updatedEmployee).isNotNull();
    assertThat(updatedEmployee.getFirstName()).isEqualTo("Jane");
  }

  // JUnit test for delete employee
  @Test
  @DisplayName("Delete employee")
  public void givenEmployeeId_whenDelete_thenEmployeeShouldBeDeleted() {
    // given
    employeeRepository.save(employee);

    // when
    employeeRepository.deleteById(employee.getId());

    // then
    assertThat(employeeRepository.findById(employee.getId())).isEmpty();
  }

}