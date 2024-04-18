package com.financing.app.expenses.application.port.in;

import com.financing.app.budget.application.port.in.BudgetInfo;

import java.util.List;

public record MonthlyOverview(
        BudgetInfo budgetInfo,
        List<ExpenseInfo> expenses
) {
}
