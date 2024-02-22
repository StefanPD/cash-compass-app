package com.financing.app.income;

import java.math.BigDecimal;
import java.time.LocalDate;

public record IncomeRequest(
        BigDecimal amount,
        String source,
        LocalDate incomeDate,
        String description
) {
}
