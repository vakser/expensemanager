package com.example.expensemanager.repository;

import com.example.expensemanager.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Optional<Expense> findByExpenseId(String id);

    List<Expense> findByNameContainingAndDateBetweenAndUserId(String keyword, java.sql.Date startDate, Date endDate, Long id);

    List<Expense> findByUserId(Long id);

    List<Expense> findByDateBetweenAndUserId(Date startDate, Date endDate, Long id);
}
