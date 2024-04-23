package com.financing.app.budget.adapter.out.persistence;

import com.financing.app.budget.application.domain.model.BudgetDTO;
import com.financing.app.budget.application.port.in.BudgetInfo;
import com.financing.app.budget.application.port.out.LoadBudgetPort;
import com.financing.app.expenses.application.port.out.LoadBudgetPortInterface;
import com.financing.app.user.adapter.out.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BudgetPersistenceAdapter implements LoadBudgetPort, LoadBudgetPortInterface {

    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;

    @Override
    public List<BudgetInfo> loadBudget(User user) {
        return budgetRepository.findBudgetsByUser(user);
    }

    @Override
    public BudgetDTO loadBudgetByYearAndMonth(User user, int year, int month) {
        var budget = budgetRepository.findByUserAndYearAndMonth(user, year, month)
                .orElseThrow(() -> new EntityNotFoundException("Budget not found for specific criteria"));
        return budgetMapper.fromBudgetToBudgetDTO(budget);
    }

    @Override
    public void saveBudget(User user, BudgetDTO budgetDTO) {
        var budget = budgetMapper.fromBudgetDTOtoBudget(budgetDTO);
        budget.setUser(user);
        budgetRepository.save(budget);
    }

    @Override
    public void deleteBudget(User user, Long budgetId) throws EntityNotFoundException {
        budgetRepository
                .findById(budgetId)
                .map(item -> {
                    if (!Objects.equals(user.getUserId(), item.getUser().getUserId())) {
                        throw new EntityNotFoundException("Entity not found");
                    }
                    budgetRepository.delete(item);
                    return item;
                })
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void updateBudget(User user, BudgetDTO budgetDTO)throws EntityNotFoundException {
        budgetRepository
                .findById(budgetDTO.getBudgetId())
                .map(item -> {
                    if (!Objects.equals(item.getUser().getUserId(), user.getUserId())) {
                        throw new EntityNotFoundException("Entity not found");
                    }
                    item.setTotalBudget(budgetDTO.getTotalBudget());
                    item.setYear(budgetDTO.getYear());
                    item.setMonth(budgetDTO.getMonth());
                    return budgetRepository.save(item);
                })
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public com.financing.app.expenses.application.domain.model.BudgetDTO loadBudgetByDate(User user, int year, int month) {
        return budgetRepository.findByUserAndYearAndMonth(user, year, month).stream().map(
                budgetDTO -> new com.financing.app.expenses.application.domain.model.BudgetDTO(
                        budgetDTO.getBudgetId(),
                        budgetDTO.getTotalBudget(),
                        budgetDTO.getMonth(),
                        budgetDTO.getYear()
                )
        ).findFirst().orElseThrow(() -> new EntityNotFoundException("Entity for specific criteria is not found"));
    }
}
