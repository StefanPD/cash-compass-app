package com.financing.app.savings.goals;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SavingGoalInfo(
        String name,
        BigDecimal targetAmount,
        BigDecimal currentAmount,
        LocalDate startDate,
        LocalDate endDate
) {
}
