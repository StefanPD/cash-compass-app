package com.financing.app.income;

import com.financing.app.user.User;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ToString
public class IncomeDTO {
    private Long incomeId;
    private BigDecimal amount;
    private String source;
    private LocalDateTime incomeDate;
    private String description;
    private User user;
}
