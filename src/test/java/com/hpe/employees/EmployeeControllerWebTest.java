package com.hpe.employees;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Web-layer tests: HTTP mapping, JSON shape and validation, with EmployeeManager mocked. */
@WebMvcTest(EmployeeController.class)
class EmployeeControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeManager employeeManager;

    @Test
    void getEmployeesReturnsJsonList() throws Exception {
        Employees employees = new Employees();
        employees.setEmployeeList(List.of(new Employee("1", "Prem", "Tiwari", "p@example.com", "Engineer")));
        when(employeeManager.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Employees[0].employee_id").value("1"))
                .andExpect(jsonPath("$.Employees[0].first_name").value("Prem"))
                .andExpect(jsonPath("$.Employees[0].title").value("Engineer"));
    }

    @Test
    void postEmployeeReturns201() throws Exception {
        when(employeeManager.existsById("10")).thenReturn(false);
        String body = """
                {"employee_id":"10","first_name":"Ali","last_name":"Khan",
                 "email":"ali@example.com","title":"QA Engineer"}""";

        mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/employees/10")))
                .andExpect(jsonPath("$.first_name").value("Ali"));
        verify(employeeManager).addEmployee(any(Employee.class));
    }

    @Test
    void postEmployeeWithInvalidEmailReturns400() throws Exception {
        String body = """
                {"employee_id":"11","first_name":"Bad","last_name":"Email",
                 "email":"not-an-email","title":"Engineer"}""";

        mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
        verify(employeeManager, never()).addEmployee(any());
    }

    @Test
    void postEmployeeWithDuplicateIdReturns409() throws Exception {
        when(employeeManager.existsById("1")).thenReturn(true);
        String body = """
                {"employee_id":"1","first_name":"Dup","last_name":"User",
                 "email":"dup@example.com","title":"Engineer"}""";

        mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict());
    }
}
