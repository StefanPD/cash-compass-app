package com.financing.app.expenses.application.port.out;

import com.financing.app.expenses.application.domain.model.ExpenseDTO;
import com.financing.app.expenses.application.port.in.ExpenseInfo;
import com.financing.app.expenses.application.port.in.ExpensePage;
import com.financing.app.user.adapter.out.User;
import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface LoadExpensePort {

    List<ExpenseInfo> loadExpenses(User user);

    ExpensePage loadExpenseAtYearAndMonth(Long userId, LocalDate date, int size, int page);

    BigDecimal loadExpenseAvgForSpecificMonth(Long userId, int year, int month);

    void saveExpense(User user, ExpenseDTO expenseDto);

    List<ExpenseInfo> loadMonthlyOverview(User user, LocalDate date);

    void deleteExpense(User user, Long expenseId) throws EntityNotFoundException;

    void updateExpense(User user, ExpenseDTO expenseDTO);
}
