package com.financing.app.expenses;

import java.util.List;

public interface ExpenseService {

    List<ExpenseDTO> fetchExpensesByUserId(Long userId);

    void saveExpense(Long userId, ExpenseDTO expenseDto);
}
