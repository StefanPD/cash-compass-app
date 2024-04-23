package com.financing.app.savings_goals.application.port.in;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SavingsGoalPostRequest(
        @NotNull(message = "The name is required.")
        String name,
        @NotNull(message = "The target amount is required.")
        @DecimalMin(value = "0.01")
        BigDecimal targetAmount,
        @NotNull(message = "The current amount is required.")
        @DecimalMin(value = "0.01")
        BigDecimal currentAmount,
        @NotNull(message = "The start date is required.")
        LocalDate startDate,
        @NotNull(message = "The end date is required.")
        LocalDate endDate
) {
}
