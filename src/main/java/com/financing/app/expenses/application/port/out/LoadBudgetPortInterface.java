package com.financing.app.expenses.application.port.out;


import com.financing.app.expenses.application.domain.model.BudgetDTO;
import com.financing.app.user.adapter.out.User;

public interface LoadBudgetPortInterface {

    BudgetDTO loadBudgetByDate(User user, int year, int month);
}
