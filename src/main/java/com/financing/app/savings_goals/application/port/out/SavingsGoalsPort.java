package com.financing.app.savings_goals.application.port.out;

import com.financing.app.user.adapter.out.User;
import com.financing.app.savings_goals.application.domain.model.SavingsGoalDTO;
import com.financing.app.savings_goals.application.port.in.SavingGoalInfo;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface SavingsGoalsPort {

    List<SavingGoalInfo> loadSavingsGoals(User user);

    void updateSavingsGoal(SavingsGoalDTO savingsGoal) throws EntityNotFoundException;

    void deleteSavingsGoal(User user, Long savingGoalId) throws EntityNotFoundException;
}
