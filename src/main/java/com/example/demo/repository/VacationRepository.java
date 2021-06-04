package com.example.demo.repository;

import com.example.demo.model.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VacationRepository extends JpaRepository<Vacation, String> {

    @Query(value = "select * from vacation where employee_id = ?1", nativeQuery = true)
    List<Vacation> findByEmployeeId(String employeeId);
}
