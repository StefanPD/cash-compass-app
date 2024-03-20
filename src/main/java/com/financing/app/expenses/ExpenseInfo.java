package com.financing.app.expenses;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseInfo(
        BigDecimal amount,
        String category,
        LocalDate expenseDate,
        String description
) {
}
