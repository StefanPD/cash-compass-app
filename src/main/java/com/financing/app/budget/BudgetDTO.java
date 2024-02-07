package com.financing.app.budget;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class BudgetDTO {
    private Long incomeId;
    private BigDecimal totalBudget;
    private int month;
    private int year;
}
