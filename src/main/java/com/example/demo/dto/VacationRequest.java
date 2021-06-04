package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VacationRequest {
    private Long numberOfDays;
    private String employeeId;
    private LocalDate vacationStartDate;
}
