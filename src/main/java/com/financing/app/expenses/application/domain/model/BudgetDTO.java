package com.financing.app.expenses.application.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
@AllArgsConstructor
public class BudgetDTO {
    private Long budgetId;
    private BigDecimal totalBudget;
    private int month;
    private int year;
}
