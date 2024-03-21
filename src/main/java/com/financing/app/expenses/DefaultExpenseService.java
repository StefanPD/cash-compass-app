package com.financing.app.expenses;

import com.financing.app.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class DefaultExpenseService implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    @Override
    public List<ExpenseInfo> fetchExpensesByUserId(Long userId) {
        return expenseRepository.findExpensesByUser(new User(userId));

    }

    @Transactional
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

    @Override
    public ExpensePage fetchExpensesForSpecificMonth(Long userId, LocalDate date, int size, int page) {
        var expensePage = expenseRepository.findByUserIdAndYearAndMonth(userId, date.getYear(), date.getMonthValue(), PageRequest.of(page, size));
        List<ExpenseDTO> content = expensePage.getContent()
                .stream()
                .map(expenseMapper::fromExpenseToExpenseDTO)
                .toList();
        return new ExpensePage(content, expensePage.getNumber(), expensePage.getTotalPages(), expensePage.getTotalElements());
    }
}
