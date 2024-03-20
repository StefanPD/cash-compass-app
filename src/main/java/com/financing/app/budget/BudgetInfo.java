package com.financing.app.budget;

import java.math.BigDecimal;

public record BudgetInfo(
        BigDecimal totalBudget,
        int month,
        int year
) {
}
