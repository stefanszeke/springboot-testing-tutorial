package com.example.springtesttutorial.controller;

import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.springtesttutorial.exception.EmployeeNotFoundException;
import com.example.springtesttutorial.model.Employee;
import com.example.springtesttutorial.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private EmployeeService employeeService;

  @Autowired
  private ObjectMapper objectMapper;

  // JUnit test for create employee
  @Test
  public void givenEmployee_whenCreateEmployee_thenReturnNewEmployee() throws Exception {
    // given
    Employee employee = Employee.builder().firstName("John").lastName("Doe").email("John@mail.com").build();
    BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
        .willAnswer((invocation) -> invocation.getArgument(0));

    // when
    ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employee")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(employee)));

    // then
    result.andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(employee.getFirstName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(employee.getLastName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(employee.getEmail()));
  }

  // JUnit test for get all employees
  @Test
  public void givenEmployees_whenGetEmployees_thenReturnEmployees() throws Exception {
    // given
    Employee employee1 = Employee.builder().firstName("John").lastName("Doe").email("John@mail.com").build();
    Employee employee2 = Employee.builder().firstName("Jane").lastName("Doe").email("Jane@mail.com").build();

    List<Employee> employees = List.of(employee1, employee2);
    BDDMockito.given(employeeService.getAllEmployees()).willReturn(employees);

    // when
    ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employee"));

    // then
    result.andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(2)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value(employee1.getFirstName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value(employee1.getLastName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(employee1.getEmail()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value(employee2.getFirstName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName").value(employee2.getLastName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value(employee2.getEmail()));
  }

  // JUnit test for get employee by id
  @Test
  public void givenID_whenGetEmployeeById_thenReturnEmployee() throws Exception {
    // given
    Employee employee = Employee.builder().firstName("John").lastName("Doe").email("John@mail.com").build();

    BDDMockito.given(employeeService.getEmployeeById(ArgumentMatchers.anyLong())).willReturn(employee);

    // when
    ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employee/{id}", employee.getId()));

    // then
    response.andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(employee.getFirstName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(employee.getLastName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(employee.getEmail()));
  }

  // JUnit test for get employee by id exception
  @Test
  public void givenBadID_whenGetEmployeeById_thenThrowException() throws Exception {
    // given
    BDDMockito.given(employeeService.getEmployeeById(ArgumentMatchers.anyLong()))
        .willThrow(new EmployeeNotFoundException("Employee not found"));
    // when
    ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employee/{id}", 1L));

    // then
    response.andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  // JUnit test for update employee
  // What the test does verify is that:

  // The controller correctly routes the PATCH request to the right handler
  // method.
  // The controller correctly deserializes the JSON request body into an Employee
  // object and passes it to the service layer.
  // The controller correctly serializes the Employee object returned by the
  // service layer into a JSON response body.
  // The controller correctly sets the HTTP status code to 200 OK in the response.
  public void givenEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
    // given
    Long id = 1L;
    Employee employee = Employee.builder().id(id).firstName("John").lastName("Doe").email("John@mail.com").build();
    Employee update = Employee.builder().id(id).firstName("Jane").build();
    Employee updatedEmployee = Employee.builder().id(id).firstName("Jane").lastName("Doe").email("John@mail.com")
        .build();

    BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Employee.class)))
        .willReturn(updatedEmployee);

    // when
    ResultActions response = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/employee/{id}", employee.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(update)));

    // then
    response.andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(updatedEmployee.getFirstName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(updatedEmployee.getLastName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(updatedEmployee.getEmail()));
  }

  @Test
  public void givenNonExistingId_whenUpdateEmployee_thenThrowEmployeeNotFoundException() throws Exception {
    // given
    Employee update = Employee.builder().id(1L).firstName("Jane").build();

    BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Employee.class)))
        .willThrow(new EmployeeNotFoundException("Employee not found for id 1"));

    // when
    ResultActions response = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/employee/{id}", update.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(update)));

    // then
    response.andExpect(MockMvcResultMatchers.status().isNotFound())
        .andDo(MockMvcResultHandlers.print());
  }

  // JUnit test for delete employee
  @Test
  public void givenEmployeeId_whenDeleteEmployee_thenEmployeeIsDeleted() throws Exception {
    // given
    Long id = 1L;
    Employee employee = Employee.builder().id(id).firstName("John").lastName("Doe").email("John@mail.com").build();
    BDDMockito.doNothing().when(employeeService).deleteEmployee(ArgumentMatchers.anyLong());

    // when
    ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/employee/{id}", employee.getId()));

    // then
    response.andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.content().string("Employee deleted successfully"));

    BDDMockito.verify(employeeService, Mockito.times(1)).deleteEmployee(id);
  }
}
