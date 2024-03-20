package com.financing.app.savings.goals;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

public interface SavingsGoalsService {
    List<SavingsGoalDTO> fetchSavingsGoalsByUserId(Long userId);

    void updateSavingsGoal(SavingsGoalDTO savingsGoal) throws EntityNotFoundException;
}
