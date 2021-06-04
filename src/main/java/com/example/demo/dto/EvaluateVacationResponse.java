package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EvaluateVacationResponse {
    private String message;
    private LocalDate vacationStartDate;
    private LocalDate vacationEndDate;
}
