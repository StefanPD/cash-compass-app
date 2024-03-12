package com.financing.app.expenses;

import com.financing.app.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DefaultExpenseService implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    public DefaultExpenseService(ExpenseRepository expenseRepository, ExpenseMapper expenseMapper) {
        this.expenseRepository = expenseRepository;
        this.expenseMapper = expenseMapper;
    }

    @Override
    public List<ExpenseDTO> fetchExpensesByUserId(Long userId) {
        return expenseRepository.findExpensesByUser(new User(userId))
                .stream()
                .map(expenseMapper::fromExpenseToExpenseDTO)
                .toList();
    }

    @Override
    public void saveExpense(Long userId, ExpenseDTO expenseDto) throws EntityNotFoundException {
        var expense = expenseMapper.fromExpenseDTOtoExpense(expenseDto);
        expense.setUser(new User(userId));
        try {
            expenseRepository.save(expense);
        } catch (DataIntegrityViolationException e) {
            log.error("Expense failed to save - api/v1/expense/{userId}/expenses, for userId: {} and expense: {}", userId, expenseDto);
            throw new EntityNotFoundException("User with this Id doesn't exist");
        }
    }
}
