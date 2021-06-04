package com.example.demo.model;

import com.example.demo.model.Enums.VacationStatus;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "vacation")
public class Vacation {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "vacation_id")
    private String id;

    @Column(name = "vacation_days_to_use")
    private Long vacationDaysToUse;

    @Enumerated(EnumType.STRING)
    private VacationStatus vacationStatus;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "vacation_start_date")
    private LocalDate vacationStartDate;

    @Column(name = "vacation_end_date")
    private LocalDate vacationEndDate;
}
