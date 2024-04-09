package com.financing.app.application.budget.domain.service;

import com.financing.app.adapter.user.out.User;
import com.financing.app.application.budget.port.in.BudgetExpensesDiff;
import com.financing.app.application.budget.port.in.BudgetInfo;
import com.financing.app.application.budget.port.out.LoadBudgetPort;
import com.financing.app.application.expenses.port.out.LoadExpensePort;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class BudgetUseCase {
    private final LoadBudgetPort loadBudgetPort;
    private final LoadExpensePort loadExpensePort;

    public List<BudgetInfo> fetchBudgetsById(Long userId) {
        return loadBudgetPort.loadBudget(new User(userId));
    }

    @Transactional
    public BudgetExpensesDiff fetchBudgetsVsExpense(Long userId, LocalDate date) throws EntityNotFoundException {
        var month = date.getMonthValue();
        var year = date.getYear();
        var budget = loadBudgetPort.loadBudgetByYearAndMonth(new User(userId), year, month);
        var totalExpenses = loadExpensePort.loadExpenseAvgForSpecificMonth(userId, year, month);
        var difference = budget.getTotalBudget().subtract(totalExpenses);
        return new BudgetExpensesDiff(budget, difference);
    }
}
