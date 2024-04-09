package com.financing.app.budget.application.port.in;

import java.math.BigDecimal;

public record BudgetInfo(
        BigDecimal totalBudget,
        int month,
        int year
) {
}
