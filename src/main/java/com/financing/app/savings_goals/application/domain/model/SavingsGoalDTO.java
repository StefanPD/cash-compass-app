package com.financing.app.savings_goals.application.domain.model;

import com.financing.app.user.adapter.out.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ToString
@AllArgsConstructor
public class SavingsGoalDTO {
    private Long savingGoalId;
    private String name;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private User user;
}