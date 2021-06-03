package com.example.demo.controller;


import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import com.example.demo.util.ApiPaths;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
@Api(value = ApiPaths.EmployeeCtrl.CTRL)
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping(value="get-all")
    public List<Employee> getEmployees(){
        //TODO : Mapper kullan
        //Todo: Sl4fj
        return employeeService.getEmployees();
    }

    @GetMapping(value="get/{id}")
    public Employee getEmployeeById(@PathVariable String id){
        return employeeService.findById(id);
    }

    @PostMapping(value="save-employee")
    public Employee saveEmployee(@RequestBody Employee employee){
        return employeeService.saveEmployee(employee);
    }

    @DeleteMapping(value="{id}")
    public void deleteEmployee(@PathVariable String id){
        employeeService.deleteEmployee(id);
    }

}
