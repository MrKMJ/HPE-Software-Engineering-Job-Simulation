package com.example.restservice;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/** Wrapper returned by GET /employees: {"Employees": [ ... ]}. */
public class Employees {

    @JsonProperty("Employees")
    private List<Employee> employeeList;

    public List<Employee> getEmployeeList() {
        if (employeeList == null) {
            employeeList = new ArrayList<>();
        }
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }
}
