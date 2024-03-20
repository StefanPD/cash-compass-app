package com.financing.app.budget;

import java.time.LocalDate;
import java.util.List;

public interface BudgetService {

    List<BudgetInfo> fetchBudgetsById(Long userId);

    BudgetVsExpenseDTO fetchBudgetsVsExpense(Long userId, LocalDate date);
}
