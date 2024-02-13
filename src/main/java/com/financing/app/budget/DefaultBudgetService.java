package com.financing.app.budget;

import com.financing.app.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultBudgetService implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;

    public DefaultBudgetService(BudgetRepository budgetRepository, BudgetMapper budgetMapper) {
        this.budgetRepository = budgetRepository;
        this.budgetMapper = budgetMapper;
    }

    @Override
    public List<BudgetDTO> fetchBudgetsById(Long userId) {
        return budgetRepository.findBudgetsByUser(new User(userId))
                .stream()
                .map(budgetMapper::fromBudgetToBudgetDTO)
                .toList();
    }
}
