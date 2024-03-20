package com.financing.app.budget;

import com.financing.app.expenses.Expense;
import com.financing.app.expenses.ExpenseMapper;
import com.financing.app.expenses.ExpenseRepository;
import com.financing.app.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class DefaultBudgetService implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;
    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    @Override
    public List<BudgetDTO> fetchBudgetsById(Long userId) {
        return budgetRepository.findBudgetsByUser(new User(userId))
                .stream()
                .map(budgetMapper::fromBudgetToBudgetDTO)
                .toList();
    }

    @Override
    public BudgetVsExpenseDTO fetchBudgetsVsExpense(Long userId, LocalDate date) throws EntityNotFoundException {
        var month = date.getMonthValue();
        var year = date.getYear();
        var budget = budgetRepository.findByUserAndYearAndMonth(new User(userId), year, month)
                .orElseThrow(() -> new EntityNotFoundException("Budget not found for specific criteria"));
        var expenses = expenseRepository.findByUserIdAndYearAndMonth(userId, year, month);
        var totalExpenses = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        var difference = budget.getTotalBudget().subtract(totalExpenses);
        var budgetDto = budgetMapper.fromBudgetToBudgetDTO(budget);
        var expensesDto = expenses.stream()
                .map(expenseMapper::fromExpenseToExpenseDTO)
                .toList();
        return new BudgetVsExpenseDTO(budgetDto, expensesDto, difference);
    }
}
