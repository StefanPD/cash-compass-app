package com.financing.app.savings_goals.application.domain.service;

import com.financing.app.expenses.application.domain.model.ExpenseDTO;
import com.financing.app.user.adapter.out.User;
import com.financing.app.savings_goals.application.domain.model.SavingsGoalDTO;
import com.financing.app.savings_goals.application.port.in.SavingGoalInfo;
import com.financing.app.savings_goals.application.port.out.SavingsGoalsPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class SavingsGoalsUseCase {

    private final SavingsGoalsPort savingsGoalsPort;

    @Transactional
    public List<SavingGoalInfo> fetchSavingsGoalsByUserId(Long userId) {
        return savingsGoalsPort.loadSavingsGoals(new User(userId));
    }

    @Transactional
    public void updateSavingsGoal(User user, SavingsGoalDTO savingsGoal) throws EntityNotFoundException {
        savingsGoalsPort.updateSavingsGoal(user, savingsGoal);
    }

    @Transactional
    public void deleteSavingsGoalWithIds(Long userId, Long savingGoalId) throws EntityNotFoundException {
        savingsGoalsPort.deleteSavingsGoal(new User(userId), savingGoalId);
    }

    public void saveSavingsGoals(User user, SavingsGoalDTO savingsGoalDTO) {
        savingsGoalsPort.saveSavingsGoals(user, savingsGoalDTO);
    }
}