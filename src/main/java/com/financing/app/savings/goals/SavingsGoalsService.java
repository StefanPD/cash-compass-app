package com.financing.app.savings.goals;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

public interface SavingsGoalsService {
    List<SavingGoalInfo> fetchSavingsGoalsByUserId(Long userId);

    void updateSavingsGoal(SavingsGoalDTO savingsGoal) throws EntityNotFoundException;

    void deleteSavingsGoalWithIds(Long userId, Long savingGoalId) throws EntityNotFoundException;
}
