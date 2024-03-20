package com.financing.app.savings.goals;

import com.financing.app.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DefaultSavingsGoalsService implements SavingsGoalsService {

    private final SavingsGoalRepository savingsGoalRepository;
    private final SavingsGoalMapper savingsGoalMapper;

    @Override
    public List<SavingsGoalDTO> fetchSavingsGoalsByUserId(Long userId) {
        return savingsGoalRepository.findSavingsGoalsByUser(new User(userId))
                .stream()
                .map(savingsGoalMapper::fromSavingsGoalToSavingGoalsDTO)
                .toList();
    }

    @Override
    public void updateSavingsGoal(SavingsGoalDTO savingsGoal) throws EntityNotFoundException {
        savingsGoalRepository
                .findById(savingsGoal.getSavingGoalId())
                .map(item -> {
                    item.setName(savingsGoal.getName());
                    item.setCurrentAmount(savingsGoal.getCurrentAmount());
                    item.setTargetAmount(savingsGoal.getTargetAmount());
                    item.setStartDate(savingsGoal.getStartDate());
                    item.setEndDate(savingsGoal.getEndDate());
                    return savingsGoalRepository.save(item);
                })
                .orElseThrow(EntityNotFoundException::new);
    }
}