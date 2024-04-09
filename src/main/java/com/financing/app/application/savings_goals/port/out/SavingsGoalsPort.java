package com.financing.app.application.savings_goals.port.out;

import com.financing.app.adapter.user.out.User;
import com.financing.app.application.savings_goals.domain.model.SavingsGoalDTO;
import com.financing.app.application.savings_goals.port.in.SavingGoalInfo;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface SavingsGoalsPort {

    List<SavingGoalInfo> loadSavingsGoals(User user);

    void updateSavingsGoal(SavingsGoalDTO savingsGoal) throws EntityNotFoundException;

    void deleteSavingsGoal(User user, Long savingGoalId) throws EntityNotFoundException;
}
