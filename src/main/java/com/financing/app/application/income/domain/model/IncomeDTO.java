package com.financing.app.application.income.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ToString
@AllArgsConstructor
public class IncomeDTO {
    private Long incomeId;
    private BigDecimal amount;
    private String source;
    private LocalDate incomeDate;
    private String description;

    public IncomeDTO(BigDecimal amount, String source, LocalDate incomeDate, String description) {
        this.amount = amount;
        this.source = source;
        this.incomeDate = incomeDate;
        this.description = description;
    }

    public IncomeDTO() {}
}