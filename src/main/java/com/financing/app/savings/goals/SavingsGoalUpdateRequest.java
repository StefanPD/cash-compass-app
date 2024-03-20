package com.financing.app.savings.goals;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class SavingsGoalUpdateRequest {
        @NotNull(message = "The id is required.")
        private Long savingGoalId;
        @NotNull(message = "The name is required.")
        private String name;
        @NotNull(message = "The target amount is required.")
        @DecimalMin(value = "0.01")
        private BigDecimal targetAmount;
        @NotNull(message = "The current amount is required.")
        @DecimalMin(value = "0.01")
        private BigDecimal currentAmount;
        @NotNull(message = "The start date is required.")
        private LocalDate startDate;
        @NotNull(message = "The end date is required.")
        private LocalDate endDate;
}
