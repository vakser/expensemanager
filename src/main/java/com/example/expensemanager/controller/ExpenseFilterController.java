package com.example.expensemanager.controller;

import com.example.expensemanager.dto.ExpenseDto;
import com.example.expensemanager.dto.ExpenseFilterDto;
import com.example.expensemanager.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.text.ParseException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ExpenseFilterController {
    private final ExpenseService expenseService;

    @GetMapping("/filterExpenses")
    public String filterExpenses(@ModelAttribute("filter") ExpenseFilterDto expenseFilterDto, Model model) throws ParseException {
        System.out.println("Filter DTO: " + expenseFilterDto);
        List<ExpenseDto> list = expenseService.getFilteredExpenses(expenseFilterDto);
        model.addAttribute("expenses", list);
        String totalExpenses = expenseService.totalExpenses(list);
        model.addAttribute("totalExpenses", totalExpenses);
        return "expenses-list";
    }
}
