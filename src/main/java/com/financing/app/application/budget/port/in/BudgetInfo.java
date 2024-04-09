package com.financing.app.application.budget.port.in;

import java.math.BigDecimal;

public record BudgetInfo(
        BigDecimal totalBudget,
        int month,
        int year
) {
}
