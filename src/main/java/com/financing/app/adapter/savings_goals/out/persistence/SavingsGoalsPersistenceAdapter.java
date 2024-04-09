package com.financing.app.adapter.savings_goals.out.persistence;

import com.financing.app.adapter.user.out.User;
import com.financing.app.application.savings_goals.domain.model.SavingsGoalDTO;
import com.financing.app.application.savings_goals.port.in.SavingGoalInfo;
import com.financing.app.application.savings_goals.port.out.SavingsGoalsPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SavingsGoalsPersistenceAdapter implements SavingsGoalsPort {

    private final SavingsGoalRepository savingsGoalRepository;
    @Override
    public List<SavingGoalInfo> loadSavingsGoals(User user) {
        return savingsGoalRepository.findSavingsGoalsByUser(user);
    }

    @Override
    public void updateSavingsGoal(SavingsGoalDTO savingsGoal) throws EntityNotFoundException {
        savingsGoalRepository
                .findById(savingsGoal.getSavingGoalId())
                .map(item -> {
                    if (!Objects.equals(item.getUser().getUserId(), savingsGoal.getUser().getUserId())) {
                        // TODO: Throw invalid access exception
                        return item;
                    }
                    item.setName(savingsGoal.getName());
                    item.setCurrentAmount(savingsGoal.getCurrentAmount());
                    item.setTargetAmount(savingsGoal.getTargetAmount());
                    item.setStartDate(savingsGoal.getStartDate());
                    item.setEndDate(savingsGoal.getEndDate());
                    return savingsGoalRepository.save(item);
                })
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void deleteSavingsGoal(User user, Long savingGoalId) throws EntityNotFoundException {
        savingsGoalRepository
                .findById(savingGoalId)
                .map(item -> {
                    if (!Objects.equals(user.getUserId(), item.getUser().getUserId())) {
                        // TODO: Throw invalid access exception
                        return item;
                    }
                    savingsGoalRepository.delete(item);
                    return item;
                })
                .orElseThrow(EntityNotFoundException::new);
    }
}
