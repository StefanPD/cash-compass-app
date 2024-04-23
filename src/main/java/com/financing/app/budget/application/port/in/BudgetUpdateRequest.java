package com.financing.app.budget.application.port.in;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class BudgetUpdateRequest {
    @NotNull(message = "The id is required.")
    private Long budgetId;
    @NotNull(message = "The amount is required.")
    @DecimalMin(value = "0.01")
    BigDecimal totalBudget;
    @NotNull(message = "The month is required.")
    int month;
    @NotNull(message = "The year is required.")
    int year;
}
