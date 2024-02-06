package com.financing.app.savings.goals;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SavingsGoalsService {
    List<SavingsGoalDTO> fetchSavingsGoalsByUserId(Long userId);
}
