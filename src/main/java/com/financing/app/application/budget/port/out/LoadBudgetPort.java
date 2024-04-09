package com.financing.app.application.budget.port.out;

import com.financing.app.adapter.user.out.User;
import com.financing.app.application.budget.domain.model.BudgetDTO;
import com.financing.app.application.budget.port.in.BudgetInfo;

import java.util.List;

public interface LoadBudgetPort {

    List<BudgetInfo> loadBudget(User user);

    BudgetDTO loadBudgetByYearAndMonth(User user, int year, int month);
}
