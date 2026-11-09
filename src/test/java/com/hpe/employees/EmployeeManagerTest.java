package com.hpe.employees;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmployeeManagerTest {

    private EmployeeManager manager;

    @BeforeEach
    void setUp() {
        manager = new EmployeeManager();
    }

    @Test
    void startsWithSeededEmployees() {
        assertEquals(4, manager.getAllEmployees().getEmployeeList().size());
        assertTrue(manager.existsById("1"));
    }

    @Test
    void addEmployeeAppendsToList() {
        Employee e = new Employee("5", "Ali", "Khan", "ali.khan@example.com", "QA Engineer");
        manager.addEmployee(e);

        assertEquals(5, manager.getAllEmployees().getEmployeeList().size());
        assertTrue(manager.existsById("5"));
        assertEquals(e, manager.getAllEmployees().getEmployeeList().get(4));
    }

    @Test
    void existsByIdIsFalseForUnknownId() {
        assertFalse(manager.existsById("999"));
    }
}
