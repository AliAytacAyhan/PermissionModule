package com.example.demo.service.ımpl;

import com.example.demo.dto.EvaluateVacationRequest;
import com.example.demo.dto.EvaluateVacationResponse;
import com.example.demo.dto.VacationRequest;
import com.example.demo.dto.VacationResponse;
import com.example.demo.model.Employee;
import com.example.demo.model.Enums.VacationStatus;
import com.example.demo.model.Vacation;
import com.example.demo.repository.VacationRepository;
import com.example.demo.service.MessageService;
import com.example.demo.service.VacationService;
import com.example.demo.util.Constants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VacationServiceImpl implements VacationService {

    private final MessageService messageService;

    private final EmployeeServiceImpl employeeService;

    private final VacationRepository vacationRepository;

    private static final String TOO_MUCH_DAYS = "com.vacation.too.much.days";

    private static final String VACATION_WAITING = "com.vacation.waiting.message";

    private static final String VACATION_GRANTED = "com.vacation.granted.message";

    private static final String VACATION_REJECTED = "com.vacation.rejected.message";

    private static final String ONE_YEAR_EMPLOYEE_TOO_MUCH_DAYS = "com.vacation.new.employee.too.much.days.message";

    private static Logger logger = LoggerFactory.getLogger(VacationServiceImpl.class);

    @Override
    public VacationResponse requestVacation(VacationRequest vacationRequest) {
        final VacationResponse vacationResponse = new VacationResponse();
        final Employee employee = employeeService.findById(vacationRequest.getEmployeeId());
        final Long vacationDaysEarned = employee.getVacationDaysEarned();
        employee.setVacationDaysEarned(vacationDaysEarned - vacationRequest.getNumberOfDays());
        final Vacation vacation = new Vacation();
        vacation.setEmployee(employee);
        vacation.setVacationDaysToUse(vacationRequest.getNumberOfDays());
        vacation.setVacationStatus(VacationStatus.WAITING);
        Vacation savedVacation = vacationRepository.save(vacation);
        vacationResponse.setMessage(messageService.getMessage(VACATION_WAITING));
        vacationResponse.setVacationId(savedVacation.getId());
        logger.info(messageService.getMessage(VACATION_WAITING));
        return vacationResponse;
    }

    @Override
    public EvaluateVacationResponse evaluateVacation(@RequestBody EvaluateVacationRequest evaluateVacationRequest) {
        final EvaluateVacationResponse vacationResponse = new EvaluateVacationResponse();
        final Employee employee = employeeService.findById(evaluateVacationRequest.getEmployeeId());
        final Optional<Vacation> vacationOptional = vacationRepository.findById(evaluateVacationRequest.getVacationId());

        if (vacationOptional.isPresent()) {
            final Vacation vacation = vacationOptional.get();
            if (VacationStatus.REJECTED.equals(evaluateVacationRequest.getVacationStatus())) {
                final Long vacationDaysEarned = employee.getVacationDaysEarned();
                employee.setVacationDaysEarned(vacationDaysEarned + vacation.getVacationDaysToUse());
                vacationResponse.setMessage(messageService.getMessage(VACATION_REJECTED));
                logger.info(messageService.getMessage(VACATION_REJECTED));
            }

            if (Constants.ZERO.equals(employee.getWorkedYear())) {
                if (vacation.getVacationDaysToUse() > Constants.PRE_GRANTED_VACATION_DAYS) {
                    vacationResponse.setMessage(messageService.getMessage(ONE_YEAR_EMPLOYEE_TOO_MUCH_DAYS));
                } else {
                    Long updatedAdvancedDays = Constants.PRE_GRANTED_VACATION_DAYS - vacation.getVacationDaysToUse();
                    employee.setAdvancedVacationDays(updatedAdvancedDays);
                    employee.setVacationDaysEarned(-vacation.getVacationDaysToUse());
                    vacationResponse.setMessage(messageService.getMessage(VACATION_GRANTED));
                    vacation.setVacationStatus(VacationStatus.GRANTED);
                    vacationRepository.save(vacation);
                }
            }
            if (vacation.getVacationDaysToUse() > employee.getVacationDaysEarned()) {
                vacationResponse.setMessage(messageService.getMessage(TOO_MUCH_DAYS));
                vacation.setVacationStatus(VacationStatus.REJECTED);
                vacationRepository.save(vacation);
                logger.info(messageService.getMessage(TOO_MUCH_DAYS));
            } else {
                vacationResponse.setMessage(messageService.getMessage(VACATION_GRANTED));
                vacation.setVacationStatus(VacationStatus.GRANTED);
                vacationRepository.save(vacation);
                logger.info(messageService.getMessage(VACATION_GRANTED));
            }
        }
        return vacationResponse;
    }
}


