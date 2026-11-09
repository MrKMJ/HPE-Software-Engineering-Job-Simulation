package com.example.restservice;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeManager employeeManager;

    public EmployeeController(EmployeeManager employeeManager) {
        this.employeeManager = employeeManager;
    }

    /** GET /employees: returns every employee. */
    @GetMapping(produces = "application/json")
    public Employees getEmployees() {
        return employeeManager.getAllEmployees();
    }

    /**
     * POST /employees: adds an employee.
     * 201 Created with a Location header on success, 409 Conflict if the id is taken,
     * 400 Bad Request if a field is missing or the email is invalid.
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody Employee employee) {
        if (employeeManager.existsById(employee.getEmployeeId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        employeeManager.addEmployee(employee);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(employee.getEmployeeId())
                .toUri();
        return ResponseEntity.created(location).body(employee);
    }
}
