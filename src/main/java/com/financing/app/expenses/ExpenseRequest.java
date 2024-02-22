package com.financing.app.expenses;

import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest(
        BigDecimal amount,
        String category,
        LocalDate expenseDate,
        String description
) {
}
