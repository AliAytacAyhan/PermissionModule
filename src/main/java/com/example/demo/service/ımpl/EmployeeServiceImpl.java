package com.example.demo.service.ımpl;

import com.example.demo.model.Employee;
import com.example.demo.model.Vacation;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.VacationRepository;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.MessageService;
import com.example.demo.util.Constants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final MessageService messageService;

    private final EmployeeRepository employeeRepository;

    private final VacationRepository vacationRepository;

    private static final String EMPLOYEE_SAVED ="com.employee.is.saved";

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Override
    public List<Employee> getEmployees() {
        final List<Employee> employees =employeeRepository.findAll();
        employees.forEach(employee ->
        employee.setVacationList(vacationRepository.findByEmployeeId(employee.getId())));
        return employees;
    }

    @Override
    public Employee findById(String id) {
        final Optional<Employee> employeeOptional = employeeRepository.findById(id);
        final List<Vacation> vacations = vacationRepository.findByEmployeeId(id);
        employeeOptional.get().setVacationList(vacations);
        return employeeOptional.orElse(null);
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        final LocalDate now = LocalDate.now();
        employee.setWorkedYear(Long.valueOf(now.getYear() - employee.getStartedYear().getYear()));

        if (employee.getWorkedYear() == 0L) {
            employee.setAdvancedVacationDays(5L);
            employee.setVacationDaysEarned(0L);
        } else {
            employee.setVacationDaysEarned(calculatePermissionDays(employee.getWorkedYear()));
        }
        logger.info(messageService.getMessage(EMPLOYEE_SAVED));
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(String id) {
        employeeRepository.deleteById(id);
    }

    private Long calculatePermissionDays(final Long workedYear) {
        long permissionDays;

        if (workedYear <= Constants.WORKED_YEAR_FIVE) {
            permissionDays = Constants.PERMISSION_DAYS_FIFTEEN * workedYear;
        } else if (workedYear <= Constants.WORKED_YEAR_TEN) {
            permissionDays = Constants.PERMISSION_DAYS_EIGHTEEN * workedYear;
        } else {
            permissionDays = Constants.PERMISSION_DAYS_TWENTY_FOUR * workedYear;
        }
        return permissionDays;
    }
}
