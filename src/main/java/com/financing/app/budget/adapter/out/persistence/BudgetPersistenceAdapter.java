package com.financing.app.budget.adapter.out.persistence;

import com.financing.app.expenses.adapter.out.persistence.ExpenseRepository;
import com.financing.app.user.adapter.out.User;
import com.financing.app.budget.application.domain.model.BudgetDTO;
import com.financing.app.budget.application.port.in.BudgetInfo;
import com.financing.app.budget.application.port.out.LoadBudgetPort;
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
