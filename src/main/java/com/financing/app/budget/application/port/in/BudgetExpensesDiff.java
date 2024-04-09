package com.financing.app.budget.application.port.in;

import com.financing.app.budget.application.domain.model.BudgetDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

//TODO: find the strategy for differentiating between DTO and Response classes
@Data
@ToString
@AllArgsConstructor
public class BudgetExpensesDiff {
    BudgetDTO budget;
    BigDecimal diff;
}
