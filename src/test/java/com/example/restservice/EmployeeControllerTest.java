package com.example.restservice;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Pure unit tests: the controller is tested in isolation with a Mockito mock of EmployeeManager. */
@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeManager employeeManager;

    @InjectMocks
    private EmployeeController controller;

    @BeforeEach
    void bindRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/employees");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @AfterEach
    void clearRequest() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void getEmployeesReturnsManagerList() {
        Employees employees = new Employees();
        employees.setEmployeeList(List.of(new Employee("1", "Prem", "Tiwari", "p@example.com", "Engineer")));
        when(employeeManager.getAllEmployees()).thenReturn(employees);

        Employees result = controller.getEmployees();

        assertSame(employees, result);
        assertEquals(1, result.getEmployeeList().size());
        verify(employeeManager).getAllEmployees();
    }

    @Test
    void addEmployeeReturnsCreatedWithLocation() {
        Employee e = new Employee("10", "Ali", "Khan", "ali@example.com", "QA Engineer");
        when(employeeManager.existsById("10")).thenReturn(false);

        ResponseEntity<Employee> response = controller.addEmployee(e);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(e, response.getBody());
        assertEquals("http://localhost/employees/10", response.getHeaders().getLocation().toString());
        verify(employeeManager).addEmployee(e);
    }

    @Test
    void addEmployeeWithDuplicateIdReturnsConflict() {
        Employee e = new Employee("1", "Dup", "User", "dup@example.com", "Engineer");
        when(employeeManager.existsById("1")).thenReturn(true);

        ResponseEntity<Employee> response = controller.addEmployee(e);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
        verify(employeeManager, never()).addEmployee(any());
    }
}
