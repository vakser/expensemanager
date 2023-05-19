package com.example.expensemanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseFilterDto {
    private String keyword;
    private String sortBy;
    private String startDate;
    private String endDate;

    public ExpenseFilterDto(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
