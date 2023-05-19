package com.example.expensemanager.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDto {
    private Long id;
    private String expenseId;
    @NotBlank(message = "Expense name should not be empty")
    @Size(min = 3, message = "Expense name should consist of at least {min} characters")
    private String name;
    private String description;
    @NotNull(message = "Expense amount should not be empty")
    @Min(value = 1, message = "Expense amount should not be less than 1")
    private BigDecimal amount;
    private Date date;
    private String dateString;

}
