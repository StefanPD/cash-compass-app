package com.financing.app.expenses;

import com.financing.app.user.User;
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
    private User user;
}
