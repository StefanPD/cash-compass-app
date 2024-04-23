package com.financing.app.savings_goals.application.port.out;

import com.financing.app.savings_goals.application.domain.model.SavingsGoalDTO;
import com.financing.app.savings_goals.application.port.in.SavingGoalInfo;
import com.financing.app.user.adapter.out.User;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface SavingsGoalsPort {

    List<SavingGoalInfo> loadSavingsGoals(User user);

    void updateSavingsGoal(User user, SavingsGoalDTO savingsGoal) throws EntityNotFoundException;

    void deleteSavingsGoal(User user, Long savingGoalId) throws EntityNotFoundException;

    void saveSavingsGoals(User user, SavingsGoalDTO savingsGoalDTO);
}
