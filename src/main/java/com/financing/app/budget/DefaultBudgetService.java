package com.financing.app.budget;

import com.financing.app.expenses.ExpenseRepository;
import com.financing.app.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class DefaultBudgetService implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;
    private final ExpenseRepository expenseRepository;

    @Override
    public List<BudgetInfo> fetchBudgetsById(Long userId) {
        return budgetRepository.findBudgetsByUser(new User(userId));
    }

    @Transactional
    @Override
    public BudgetExpensesDiff fetchBudgetsVsExpense(Long userId, LocalDate date) throws EntityNotFoundException {
        var month = date.getMonthValue();
        var year = date.getYear();
        var budget = budgetRepository.findByUserAndYearAndMonth(new User(userId), year, month)
                .orElseThrow(() -> new EntityNotFoundException("Budget not found for specific criteria"));
        var totalExpenses = expenseRepository.calculateExpenseForSpecificMonth(userId, year, month);
        var difference = budget.getTotalBudget().subtract(totalExpenses);
        var budgetDto = budgetMapper.fromBudgetToBudgetDTO(budget);
        return new BudgetExpensesDiff(budgetDto, difference);
    }
}
