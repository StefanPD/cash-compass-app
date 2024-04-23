package com.financing.app.budget.application.domain.model;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class BudgetDTO {
    private Long budgetId;
    private BigDecimal totalBudget;
    private int month;
    private int year;


    public BudgetDTO(BigDecimal totalBudget, int month, int year) {
        this.totalBudget = totalBudget;
        this.month = month;
        this.year = year;
    }

    public BudgetDTO(Long budgetId, BigDecimal totalBudget, int month, int year) {
        this.budgetId = budgetId;
        this.totalBudget = totalBudget;
        this.month = month;
        this.year = year;
    }

    public BudgetDTO() {
    }
}
