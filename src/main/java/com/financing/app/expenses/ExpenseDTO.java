package com.financing.app.expenses;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ToString
public class ExpenseDTO {

    private Long expensesId;
    private BigDecimal amount;
    private String category;
    private LocalDateTime expenseDate;
    private String description;
}
