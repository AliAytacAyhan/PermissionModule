package com.example.demo.service;

import com.example.demo.model.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> getEmployees();

    Employee findById(String id);

    Employee saveEmployee(Employee employee);

    void deleteEmployee(String id);
}
