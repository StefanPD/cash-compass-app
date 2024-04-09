package com.financing.app.application.expenses.port.out;

import com.financing.app.adapter.user.out.User;
import com.financing.app.application.expenses.domain.model.ExpenseDTO;
import com.financing.app.application.expenses.port.in.ExpenseInfo;
import com.financing.app.application.expenses.port.in.ExpensePage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface LoadExpensePort {

    List<ExpenseInfo> loadExpenses(User user);

    ExpensePage loadExpenseAtYearAndMonth(Long userId, LocalDate date, int size, int page);

    BigDecimal loadExpenseAvgForSpecificMonth(Long userId, int year, int month);

    void saveExpense(User user, ExpenseDTO expenseDto);
}
