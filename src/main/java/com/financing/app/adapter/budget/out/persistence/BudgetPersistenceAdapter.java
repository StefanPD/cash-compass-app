package com.financing.app.adapter.budget.out.persistence;

import com.financing.app.adapter.expenses.out.persistence.ExpenseRepository;
import com.financing.app.adapter.user.out.User;
import com.financing.app.application.budget.domain.model.BudgetDTO;
import com.financing.app.application.budget.port.in.BudgetInfo;
import com.financing.app.application.budget.port.out.LoadBudgetPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BudgetPersistenceAdapter implements LoadBudgetPort{

    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;
    private final ExpenseRepository expenseRepository;

    @Override
    public List<BudgetInfo> loadBudget(User user) {
        return budgetRepository.findBudgetsByUser(user);
    }

    @Override
    public BudgetDTO loadBudgetByYearAndMonth(User user, int year, int month) {
        var budget = budgetRepository.findByUserAndYearAndMonth(user, year, month)
                .orElseThrow(() -> new EntityNotFoundException("Budget not found for specific criteria"));
        return budgetMapper.fromBudgetToBudgetDTO(budget);
    }
}
