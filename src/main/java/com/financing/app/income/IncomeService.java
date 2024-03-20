package com.financing.app.income;

import java.time.LocalDate;
import java.util.List;

public interface IncomeService {

    List<IncomeInfo> fetchIncomesByUserId(Long userId);

    List<IncomeDTO> fetchIncomesByHistory(Long userId, LocalDate startDate, LocalDate endDate);

    void saveIncome(Long userId, IncomeDTO incomeDTO);
}
