package com.financing.app.savings.goals;

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
        return savingsGoalRepository.findSavingsGoalsByUserId(userId)
                .stream()
                .map(savingsGoalMapper::fromSavingsGoalToSavingGoalsDTO)
                .toList();
    }
}
