package com.example.expensemanager.controller;

import com.example.expensemanager.dto.ExpenseDto;
import com.example.expensemanager.dto.ExpenseFilterDto;
import com.example.expensemanager.service.ExpenseService;
import com.example.expensemanager.util.DateTimeUtil;
import com.example.expensemanager.validator.ExpenseValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @GetMapping("/expenses")
    public String showExpenseList(Model model) {
        List<ExpenseDto> list = expenseService.getAllExpenses();
        model.addAttribute("expenses", list);
        model.addAttribute("filter", new ExpenseFilterDto(DateTimeUtil.getCurrentMonthStartDate(), DateTimeUtil.getCurrentMonthDate()));
        String totalExpenses = expenseService.totalExpenses(list);
        model.addAttribute("totalExpenses", totalExpenses);
        return "expenses-list";
    }

    @GetMapping("/createExpense")
    public String showExpenseForm(Model model) {
        model.addAttribute("expense", new ExpenseDto());
        return "expense-form";
    }

    @PostMapping("/saveOrUpdateExpense")
    public String saveOrUpdateExpenseDetails(@Valid @ModelAttribute("expense") ExpenseDto expenseDto, BindingResult result) throws ParseException {
        System.out.println("Printing the Expense DTO: " + expenseDto);
        new ExpenseValidator().validate(expenseDto, result);
        if (result.hasErrors()) {
            return "expense-form";
        }
        expenseService.saveExpenseDetails(expenseDto);
        return "redirect:/expenses";
    }

    @GetMapping("/deleteExpense")
    public String deleteExpense(@RequestParam String id) {
        System.out.println("Expense id: " + id);
        expenseService.deleteExpense(id);
        return "redirect:/expenses";
    }

    @GetMapping("/updateExpense")
    public String updateExpense(@RequestParam String id, Model model) {
        System.out.println("Expense id inside update method:" + id);
        ExpenseDto expense = expenseService.getExpenseById(id);
        model.addAttribute("expense", expense);
        return "expense-form";
    }
}
