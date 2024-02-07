package com.financing.app.savings.goals;

import com.financing.app.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultSavingsGoalsService implements SavingsGoalsService {

    private final SavingsGoalRepository savingsGoalRepository;
    private final SavingsGoalMapper savingsGoalMapper;

    public DefaultSavingsGoalsService(SavingsGoalRepository savingsGoalRepository, SavingsGoalMapper savingsGoalMapper) {
        this.savingsGoalRepository = savingsGoalRepository;
        this.savingsGoalMapper = savingsGoalMapper;
    }

    @Override
    public List<SavingsGoalDTO> fetchSavingsGoalsByUserId(Long userId) {
        return savingsGoalRepository.findSavingsGoalsByUser(new User(userId))
                .stream()
                .map(savingsGoalMapper::fromSavingsGoalToSavingGoalsDTO)
                .toList();
    }
}
