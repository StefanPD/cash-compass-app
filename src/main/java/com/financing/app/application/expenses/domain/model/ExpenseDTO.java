package com.financing.app.application.expenses.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ToString
@NoArgsConstructor
public class ExpenseDTO {
    private Long expensesId;
    private BigDecimal amount;
    private String category;
    private LocalDate expenseDate;
    private String description;

    public ExpenseDTO(BigDecimal amount, String category, LocalDate expenseDate, String description) {
        this.amount = amount;
        this.category = category;
        this.expenseDate = expenseDate;
        this.description = description;
    }
}
