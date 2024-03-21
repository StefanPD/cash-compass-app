package com.financing.app.expenses;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {

    List<ExpenseInfo> fetchExpensesByUserId(Long userId);

    void saveExpense(Long userId, ExpenseDTO expenseDto);

    ExpensePage fetchExpensesForSpecificMonth(Long userId, LocalDate date, int size, int page);
}
