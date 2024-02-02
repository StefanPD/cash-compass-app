package com.financing.app.budget;

import com.financing.app.user.User;
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
    private User user;
}
