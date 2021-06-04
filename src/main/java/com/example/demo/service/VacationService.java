package com.example.demo.service;

import com.example.demo.dto.EvaluateVacationRequest;
import com.example.demo.dto.EvaluateVacationResponse;
import com.example.demo.dto.VacationRequest;
import com.example.demo.dto.VacationResponse;

public interface VacationService {

    VacationResponse requestVacation(VacationRequest vacationRequest);

    EvaluateVacationResponse evaluateVacation(EvaluateVacationRequest evaluateVacationRequest);
}
