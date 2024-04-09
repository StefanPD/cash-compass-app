package com.financing.app.application.savings_goals.port.in;

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
