package com.financing.app.income;

import java.util.List;

public interface IncomeService {

    List<IncomeDTO> fetchIncomesByUserId(Long userId);
}
