package com.financing.app.budget.application.port.out;

import com.financing.app.user.adapter.out.User;
import com.financing.app.budget.application.domain.model.BudgetDTO;
import com.financing.app.budget.application.port.in.BudgetInfo;

import java.util.List;

public interface LoadBudgetPort {

    List<BudgetInfo> loadBudget(User user);

    BudgetDTO loadBudgetByYearAndMonth(User user, int year, int month);

    void saveBudget(User user, BudgetDTO budget);

    void deleteBudget(User user, Long budgetId);

    void updateBudget(User user, BudgetDTO budgetDTO);
}
