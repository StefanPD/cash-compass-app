package com.financing.app.savings.goals;

import com.financing.app.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
    public void deleteSavingsGoalWithIds(Long userId, Long savingGoalId) throws EntityNotFoundException {
        savingsGoalRepository
                .findById(savingGoalId)
                .map( item -> {
                    if (!Objects.equals(userId, item.getUser().getUserId())) {
                        // TODO: Throw invalid access exception
                        return item;
                    }
                    savingsGoalRepository.delete(item);
                    return item;
                })
                .orElseThrow(EntityNotFoundException::new);
    }
}