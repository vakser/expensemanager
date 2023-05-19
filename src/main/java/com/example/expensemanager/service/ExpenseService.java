package com.example.expensemanager.service;

import com.example.expensemanager.dto.ExpenseDto;
import com.example.expensemanager.dto.ExpenseFilterDto;
import com.example.expensemanager.entity.Expense;
import com.example.expensemanager.entity.User;
import com.example.expensemanager.exception.ExpenseNotFoundException;
import com.example.expensemanager.repository.ExpenseRepository;
import com.example.expensemanager.util.DateTimeUtil;
import com.ibm.icu.text.NumberFormat;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public List<ExpenseDto> getAllExpenses() {
        User user = userService.getLoggedInUser();
        List<Expense> list = expenseRepository.findByDateBetweenAndUserId(Date.valueOf(LocalDate.now().withDayOfMonth(1)), Date.valueOf(LocalDate.now()), user.getId());
        return list.stream().map(this::mapToDto).toList();
    }

    private ExpenseDto mapToDto(Expense expense) {
        ExpenseDto expenseDto = modelMapper.map(expense, ExpenseDto.class);
        expenseDto.setDateString(DateTimeUtil.convertDateToString(expenseDto.getDate()));
        return expenseDto;
    }

    public ExpenseDto saveExpenseDetails(ExpenseDto expenseDto) throws ParseException {
        Expense expense = mapToEntity(expenseDto);
        if (!expense.getDate().before(new java.util.Date())) {
            throw new RuntimeException("Future date is not allowed");
        }
        expense.setUser(userService.getLoggedInUser());
        expense = expenseRepository.save(expense);
        return mapToDto(expense);
    }

    private Expense mapToEntity(ExpenseDto expenseDto) throws ParseException {
        Expense expense = modelMapper.map(expenseDto, Expense.class);
        if (expense.getId() == null) {
            expense.setExpenseId(UUID.randomUUID().toString());
        }
        expense.setDate(DateTimeUtil.convertStringToDate(expenseDto.getDateString()));
        return expense;
    }

    public void deleteExpense(String id) {
        Expense expense = getExpense(id);
        expenseRepository.delete(expense);
    }

    public ExpenseDto getExpenseById(String id) {
        Expense expense = getExpense(id);
        return mapToDto(expense);
    }

    private Expense getExpense(String id) {
        return expenseRepository.findByExpenseId(id).orElseThrow(() -> new ExpenseNotFoundException("Expense not found for id " + id));
    }

    public List<ExpenseDto> getFilteredExpenses(ExpenseFilterDto expenseFilterDto) throws ParseException {
        String keyword = expenseFilterDto.getKeyword();
        String sortBy = expenseFilterDto.getSortBy();
        String startDateString = expenseFilterDto.getStartDate();
        String endDateString = expenseFilterDto.getEndDate();
        Date startDate = !startDateString.isEmpty() ? DateTimeUtil.convertStringToDate(startDateString) : new Date(0);
        Date endDate = !endDateString.isEmpty() ? DateTimeUtil.convertStringToDate(endDateString) : new Date(System.currentTimeMillis());
        User user = userService.getLoggedInUser();
        List<Expense> list = expenseRepository.findByNameContainingAndDateBetweenAndUserId(keyword, startDate, endDate, user.getId());
        List<ExpenseDto> filteredList = list.stream().map(this::mapToDto).collect(Collectors.toList());
        if (sortBy.equals("date")) {
            filteredList.sort(((o1, o2) -> o2.getDate().compareTo(o1.getDate())));
        } else {
            filteredList.sort(((o1, o2) -> o2.getAmount().compareTo(o1.getAmount())));
        }
        return filteredList;
    }

    public String totalExpenses(List<ExpenseDto> expenses) {
        BigDecimal sum = new BigDecimal(0);
        BigDecimal total = expenses.stream().map(x -> x.getAmount().add(sum))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "ukr"));
        return format.format(total).substring(3);
    }
}
