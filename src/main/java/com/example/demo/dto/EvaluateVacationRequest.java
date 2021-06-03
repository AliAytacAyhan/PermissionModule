package com.example.demo.dto;

import com.example.demo.model.Enums.VacationStatus;
import lombok.Data;

@Data
public class EvaluateVacationRequest {
    private VacationStatus vacationStatus;
    private String employeeId;
    private String vacationId;
}
