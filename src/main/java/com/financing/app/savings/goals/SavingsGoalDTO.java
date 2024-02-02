package com.financing.app.savings.goals;

import com.financing.app.user.User;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ToString
public class SavingsGoalDTO {
    private Long savingGoalId;
    private String name;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private User user;
}
