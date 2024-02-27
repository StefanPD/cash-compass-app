package com.financing.app.expenses;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest(
        @NotNull(message = "The amount is required.")
        @DecimalMin(value = "0.01")
        BigDecimal amount,
        @NotBlank(message = "The category is required.")
        String category,
        @NotNull(message = "The expanse date is required.")
        LocalDate expenseDate,
        String description
) {
}
