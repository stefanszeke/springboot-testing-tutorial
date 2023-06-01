# unit tests
Repository Layer (SQL): For testing the repository layer, which typically involves database interactions, you can use @DataJpaTest. This annotation configures an in-memory database, configures Spring Data JPA, and scans for @Entity classes and Repository interfaces. It rolls back transactions after each test, ensuring that tests do not interfere with each other. For example:
``` java
@DataJpaTest
public class EmployeeRepositoryTest {
    @Autowired
    EmployeeRepository employeeRepository;

    // write tests here
}
```

</br >

Service Layer: For testing the service layer, you typically need to mock the repository layer to isolate the service layer from database interactions. You can use @MockBean to create and inject a Mockito mock for the repository, and @InjectMocks to create an instance of the service with the mock repository injected into it. For example:
``` java
public class EmployeeServiceTest {
    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService;

    // write tests here
}
```

</br >

Controller Layer: For testing the controller layer, you can use @WebMvcTest, which sets up a Spring MVC context and allows you to send mock HTTP requests to your controller. You can use MockMvc to send requests and assert the responses. If your controller depends on any services, you can create and inject Mockito mocks for them using @MockBean. For example:
``` java
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmployeeService employeeService;

    // write tests here
}
```

</br >
