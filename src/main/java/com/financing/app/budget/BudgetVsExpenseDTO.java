package com.financing.app.budget;

import com.financing.app.expenses.ExpenseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

//TODO: find the strategy for differentiating between DTO and Response classes
@Data
@ToString
@AllArgsConstructor
public class BudgetVsExpenseDTO {
    BudgetDTO budget;
    List<ExpenseDTO> expenses;
    BigDecimal diff;
}
