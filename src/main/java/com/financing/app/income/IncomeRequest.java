package com.financing.app.income;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record IncomeRequest(
        @NotNull(message = "The amount is required.")
        @DecimalMin(value = "0.01")
        BigDecimal amount,
        @NotNull(message = "The source is required.")
        String source,
        @NotNull(message = "The income date is required.")
        LocalDate incomeDate,
        String description
) {
}
