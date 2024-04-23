package com.financing.app.expenses.application.domain.model;

import com.financing.app.expenses.application.port.in.ExpenseUpdateRequest;
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

    public ExpenseDTO(ExpenseUpdateRequest expenseUpdateRequest) {
        this.expensesId = expenseUpdateRequest.expenseId();
        this.amount = expenseUpdateRequest.amount();
        this.category = expenseUpdateRequest.category();
        this.expenseDate = expenseUpdateRequest.expenseDate();
        this.description = expenseUpdateRequest.description();
    }
}
