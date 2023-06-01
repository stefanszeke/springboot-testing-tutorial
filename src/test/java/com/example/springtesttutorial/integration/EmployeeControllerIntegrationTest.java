package com.example.springtesttutorial.integration;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.springtesttutorial.model.Employee;
import com.example.springtesttutorial.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  public void clearDatabase() {
    employeeRepository.deleteAll();
  }

  @Test
  @DisplayName("save employee")
  public void givenEmployee_whenCreateEmployee_thenReturnNewEmployee() throws Exception {
    // given
    Employee employee = Employee.builder().firstName("John").lastName("Doe").email("John@mail.com").build();

    // when
    ResultActions result = mockMvc.perform(post("/api/v1/employee")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(employee)));

    // then
    result.andExpect(status().isCreated())
        .andExpect(jsonPath("$.firstName").value(employee.getFirstName()))
        .andExpect(jsonPath("$.lastName").value(employee.getLastName()))
        .andExpect(jsonPath("$.email").value(employee.getEmail()));

    Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());

    if (savedEmployee.isPresent()) {
      assertThat(savedEmployee.get().getFirstName()).isEqualTo(employee.getFirstName());
      assertThat(savedEmployee.get().getLastName()).isEqualTo(employee.getLastName());
      assertThat(savedEmployee.get().getEmail()).isEqualTo(employee.getEmail());
    }
  }

  @Test
  @DisplayName("get all employees")
  public void givenEmployees_whenGetEmployees_thenReturnJsonArray() throws Exception {
    // given
    Employee employee1 = Employee.builder().firstName("John").lastName("Doe").email("John@mail.com").build();
    Employee employee2 = Employee.builder().firstName("Jane").lastName("Doe").email("Jane@mail.com").build();

    employeeRepository.save(employee1);
    employeeRepository.save(employee2);

    // when
    ResultActions result = mockMvc.perform(get("/api/v1/employee")
        .contentType(MediaType.APPLICATION_JSON));

    // then
    result.andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(2)))
        .andExpect(jsonPath("$[0].firstName").value(employee1.getFirstName()))
        .andExpect(jsonPath("$[0].lastName").value(employee1.getLastName()))
        .andExpect(jsonPath("$[0].email").value(employee1.getEmail()))
        .andExpect(jsonPath("$[1].firstName").value(employee2.getFirstName()))
        .andExpect(jsonPath("$[1].lastName").value(employee2.getLastName()))
        .andExpect(jsonPath("$[1].email").value(employee2.getEmail()));
  }

  @Test
  @DisplayName("get employee by id")
  public void givenEmployee_whenGetEmployeeById_thenReturnEmployee() throws Exception {
    // given
    Employee employee = Employee.builder().firstName("John").lastName("Doe").email("John@mail.com").build();

    employeeRepository.save(employee);

    // when
    ResultActions result = mockMvc.perform(get("/api/v1/employee/{id}", employee.getId())
        .contentType(MediaType.APPLICATION_JSON));

    // then
    result.andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value(employee.getFirstName()))
        .andExpect(jsonPath("$.lastName").value(employee.getLastName()))
        .andExpect(jsonPath("$.email").value(employee.getEmail()));

    Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());

    if (savedEmployee.isPresent()) {
      assertThat(savedEmployee.get().getFirstName()).isEqualTo(employee.getFirstName());
      assertThat(savedEmployee.get().getLastName()).isEqualTo(employee.getLastName());
      assertThat(savedEmployee.get().getEmail()).isEqualTo(employee.getEmail());
    }
  }

  @Test
  @DisplayName("get employee by id exception")
  public void givenEmployee_whenGetEmployeeById_thenThrowException() throws Exception {
    // given
    Long id = 1L;

    // when
    ResultActions result = mockMvc.perform(get("/api/v1/employee/{id}", id)
        .contentType(MediaType.APPLICATION_JSON));

    // then
    result.andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Employee not found for id " + id))
        .andExpect(jsonPath("$.status").value("404 NOT_FOUND"));
  }

  @Test
  @DisplayName("update employee")
  public void givenEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
    // given
    Employee employee = Employee.builder().firstName("John").lastName("Doe").email("John@mail.com").build();

    employeeRepository.save(employee);

    Employee updatedEmployee = Employee.builder().firstName("Updated First Name").build();

    // when
    ResultActions result = mockMvc.perform(patch("/api/v1/employee/{id}", employee.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updatedEmployee)));

    // then
    result.andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value(updatedEmployee.getFirstName()))
        .andExpect(jsonPath("$.lastName").value(employee.getLastName()))
        .andExpect(jsonPath("$.email").value(employee.getEmail()));

    Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());

    if (savedEmployee.isPresent()) {
      assertThat(savedEmployee.get().getFirstName()).isEqualTo(updatedEmployee.getFirstName());
      assertThat(savedEmployee.get().getLastName()).isEqualTo(employee.getLastName());
      assertThat(savedEmployee.get().getEmail()).isEqualTo(employee.getEmail());
    }
  }

  @Test
  @DisplayName("update employee exception")
  public void givenEmployee_whenUpdateEmployee_thenThrowException() throws Exception {
    // given
    Long id = 5L;

    Employee updatedEmployee = Employee.builder().firstName("Updated First Name").build();

    // when
    ResultActions result = mockMvc.perform(patch("/api/v1/employee/{id}", id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updatedEmployee)));

    // then
    result.andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Employee not found for id " + id))
        .andExpect(jsonPath("$.status").value("404 NOT_FOUND"));
  }

  @Test 
  @DisplayName("delete employee")
  public void givenEmployee_whenDeleteEmployee_thenStatusOk() throws Exception {
    // given
    Employee employee = Employee.builder().firstName("John").lastName("Doe").email("John@mail.com").build();

    employeeRepository.save(employee);

    // when
    ResultActions result = mockMvc.perform(delete("/api/v1/employee/{id}", employee.getId())
        .contentType(MediaType.APPLICATION_JSON));

    // then
    result.andExpect(status().isOk())
          .andExpect(content().string("Employee deleted successfully"));

    Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());

    assertThat(savedEmployee).isEmpty();
  }
}
