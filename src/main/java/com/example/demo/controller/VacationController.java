package com.example.demo.controller;

import com.example.demo.dto.EvaluateVacationRequest;
import com.example.demo.dto.EvaluateVacationResponse;
import com.example.demo.dto.VacationRequest;
import com.example.demo.dto.VacationResponse;
import com.example.demo.service.VacationService;
import com.example.demo.util.ApiPaths;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("vacation")
@RequiredArgsConstructor
@Api(value = ApiPaths.VacationCtrl.CTRL)
public class VacationController {

    private final VacationService vacationService;

    @PostMapping
    public VacationResponse requestVacation(@RequestBody VacationRequest vacationRequest){
        return vacationService.requestVacation(vacationRequest);
    }

    @PutMapping
    public EvaluateVacationResponse evaluateVacation(@RequestBody EvaluateVacationRequest evaluateVacationRequest){
       return vacationService.evaluateVacation(evaluateVacationRequest);
    }


}
