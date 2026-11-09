package com.example.restservice;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * In-memory employee store, seeded with sample data. Swappable for a database-backed
 * repository without changing the controller.
 */
@Repository
public class EmployeeManager {

    private final Employees employees = new Employees();

    public EmployeeManager() {
        List<Employee> list = employees.getEmployeeList();
        list.add(new Employee("1", "Prem", "Tiwari", "prem.tiwari@example.com", "Software Engineer"));
        list.add(new Employee("2", "Vikash", "Kumar", "vikash.kumar@example.com", "Product Manager"));
        list.add(new Employee("3", "Ritesh", "Ojha", "ritesh.ojha@example.com", "Data Scientist"));
        list.add(new Employee("4", "Sarah", "Lee", "sarah.lee@example.com", "Cloud Architect"));
    }

    public synchronized Employees getAllEmployees() {
        return employees;
    }

    public synchronized boolean existsById(String employeeId) {
        return employees.getEmployeeList().stream()
                .anyMatch(e -> e.getEmployeeId().equals(employeeId));
    }

    public synchronized void addEmployee(Employee employee) {
        employees.getEmployeeList().add(employee);
    }
}
