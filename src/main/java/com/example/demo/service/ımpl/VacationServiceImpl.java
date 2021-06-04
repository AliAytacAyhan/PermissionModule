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

import java.time.DayOfWeek;
import java.time.LocalDate;
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

    private static final Logger logger = LoggerFactory.getLogger(VacationServiceImpl.class);

    @Override
    public VacationResponse requestVacation(VacationRequest vacationRequest) {

        final VacationResponse vacationResponse = new VacationResponse();
        final Employee employee = employeeService.findById(vacationRequest.getEmployeeId());

        if (vacationRequest.getNumberOfDays() > employee.getVacationDaysEarned()) {
            vacationResponse.setMessage(messageService.getMessage(TOO_MUCH_DAYS));
            return vacationResponse;
        }

        final Long vacationDaysEarned = employee.getVacationDaysEarned();
        employee.setVacationDaysEarned(vacationDaysEarned - vacationRequest.getNumberOfDays());
        final Vacation vacation = new Vacation();
        vacation.setVacationStartDate(vacationRequest.getVacationStartDate());
        vacation.setVacationEndDate(calculateEndDate(vacationRequest.getNumberOfDays(), vacationRequest.getVacationStartDate()));
        vacation.setEmployeeId(vacationRequest.getEmployeeId());
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
                vacation.setVacationStatus(VacationStatus.REJECTED);
                vacationRepository.save(vacation);
                return vacationResponse;
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
            } else {
                vacationResponse.setMessage(messageService.getMessage(VACATION_GRANTED));
                vacationResponse.setVacationStartDate(vacation.getVacationStartDate());
                vacationResponse.setVacationEndDate(vacation.getVacationEndDate());
                vacation.setVacationStatus(VacationStatus.GRANTED);

                vacationRepository.save(vacation);
                logger.info(messageService.getMessage(VACATION_GRANTED));
            }
        }
        return vacationResponse;
    }

    private LocalDate calculateEndDate(Long numberOfDays, LocalDate vacationStartDate) {
        LocalDate vacationEndDate = vacationStartDate;
        int addedDays = 0;
        while (addedDays < numberOfDays.intValue()) {
            vacationEndDate = vacationEndDate.plusDays(1);
            if (!(vacationEndDate.getDayOfWeek() == DayOfWeek.SATURDAY || vacationEndDate.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                ++addedDays;
            }
        }
        return vacationEndDate.minusDays(1);
    }
}
