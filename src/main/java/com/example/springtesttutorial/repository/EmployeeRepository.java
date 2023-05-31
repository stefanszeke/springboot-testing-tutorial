package com.example.springtesttutorial.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.springtesttutorial.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  Optional<Employee> findByEmail(String email);

  // custom query using JPQL
  @Query("SELECT e FROM Employee e WHERE e.firstName = ?1 AND e.lastName = ?2")
  Employee findByJPQL(String firstName, String lastName);
  
  // custom query using JPQL named parameter
  @Query("SELECT e FROM Employee e WHERE e.firstName = :firstName AND e.lastName = :lastName")
  Employee findByJPQLNamedParameter(String firstName, String lastName);
}
