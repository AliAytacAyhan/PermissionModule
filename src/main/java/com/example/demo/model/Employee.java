package com.example.demo.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="employee")
@Data
public class Employee {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name="employee_id")
    private String id;

    @Column(name="name")
    private String name;

    @Column(name="surname")
    private String surname;

    @Column(name="e_mail")
    private String email;

    @Column(name="worked_year")
    private Long workedYear;

    @Column(name="vacation_days_earned")
    private Long vacationDaysEarned;

    @Column(name = "advanced_vacation_days")
    private Long advancedVacationDays;

    @Column(name="started_Year")
    @DateTimeFormat(style = "yyyy-MM-dd")
    private LocalDate startedYear;

    @Transient
    private List<Vacation> vacationList;

}
