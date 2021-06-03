package com.example.demo.dto;

import lombok.Data;

@Data
public class VacationRequest {
    private Long numberOfDays;
    private String employeeId;
}
