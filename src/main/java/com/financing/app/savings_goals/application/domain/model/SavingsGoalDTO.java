package com.financing.app.savings_goals.application.domain.model;

import com.financing.app.savings_goals.application.port.in.SavingsGoalPostRequest;
import com.financing.app.savings_goals.application.port.in.SavingsGoalUpdateRequest;
import com.financing.app.user.adapter.out.User;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ToString
public class SavingsGoalDTO {
    private Long savingGoalId;
    private String name;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private User user;

    public SavingsGoalDTO(SavingsGoalPostRequest savingsGoalPostRequest) {
        this.name = savingsGoalPostRequest.name();
        this.targetAmount = savingsGoalPostRequest.targetAmount();
        this.currentAmount = savingsGoalPostRequest.currentAmount();
        this.startDate = savingsGoalPostRequest.startDate();
        this.endDate = savingsGoalPostRequest.endDate();
    }

    public SavingsGoalDTO(SavingsGoalUpdateRequest savingsGoalUpdateRequest) {
        this.savingGoalId = savingsGoalUpdateRequest.getSavingGoalId();
        this.name = savingsGoalUpdateRequest.getName();
        this.targetAmount = savingsGoalUpdateRequest.getTargetAmount();
        this.currentAmount = savingsGoalUpdateRequest.getCurrentAmount();
        this.startDate = savingsGoalUpdateRequest.getStartDate();
        this.endDate = savingsGoalUpdateRequest.getEndDate();
    }

    public SavingsGoalDTO() {}
}
