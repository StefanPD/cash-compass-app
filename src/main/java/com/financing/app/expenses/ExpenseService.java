package com.financing.app.expenses;

import java.util.List;

public interface ExpenseService {

    List<ExpenseInfo> fetchExpensesByUserId(Long userId);

    void saveExpense(Long userId, ExpenseDTO expenseDto);
}
