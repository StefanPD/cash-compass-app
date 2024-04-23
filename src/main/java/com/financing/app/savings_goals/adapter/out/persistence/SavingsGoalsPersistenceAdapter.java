package com.financing.app.savings_goals.adapter.out.persistence;

import com.financing.app.savings_goals.application.domain.model.SavingsGoalDTO;
import com.financing.app.savings_goals.application.port.in.SavingGoalInfo;
import com.financing.app.savings_goals.application.port.out.SavingsGoalsPort;
import com.financing.app.user.adapter.out.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SavingsGoalsPersistenceAdapter implements SavingsGoalsPort {

    private final SavingsGoalRepository savingsGoalRepository;
    private final SavingsGoalMapper savingsGoalMapper;

    @Override
    public List<SavingGoalInfo> loadSavingsGoals(User user) {
        return savingsGoalRepository.findSavingsGoalsByUser(user);
    }

    @Override
    public void updateSavingsGoal(User user, SavingsGoalDTO savingsGoal) throws EntityNotFoundException {
        savingsGoalRepository
                .findById(savingsGoal.getSavingGoalId())
                .map(item -> {
                    if (!Objects.equals(item.getUser().getUserId(), user.getUserId())) {
                        throw new EntityNotFoundException("Entity not found");
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
                        throw new EntityNotFoundException("Entity not found");
                    }
                    savingsGoalRepository.delete(item);
                    return item;
                })
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void saveSavingsGoals(User user, SavingsGoalDTO savingsGoalDTO) {
        var savingGoal = savingsGoalMapper.fromSavingsGoalDTOtoSavingGoal(savingsGoalDTO);
        savingGoal.setUser(user);
        System.out.println(savingGoal.getUser());
        savingsGoalRepository.save(savingGoal);
    }
}
