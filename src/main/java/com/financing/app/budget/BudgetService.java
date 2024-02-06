package com.financing.app.budget;

import java.util.List;

public interface BudgetService {

    List<BudgetDTO> fetchBudgetsById(Long userId);
}
