package com.financing.app.adapter.expenses.out.persistence;

import com.financing.app.adapter.user.out.User;
import com.financing.app.application.expenses.domain.model.ExpenseDTO;
import com.financing.app.application.expenses.port.in.ExpenseInfo;
import com.financing.app.application.expenses.port.in.ExpensePage;
import com.financing.app.application.expenses.port.out.LoadExpensePort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpensePersistenceAdapter implements LoadExpensePort {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    @Override
    public List<ExpenseInfo> loadExpenses(User user) {
        return expenseRepository.findExpensesByUser(user);
    }

    @Override
    public ExpensePage loadExpenseAtYearAndMonth(Long userId, LocalDate date, int size, int page) {
        var expensePage = expenseRepository.findByUserIdAndYearAndMonth(userId, date.getYear(), date.getMonthValue(), PageRequest.of(page, size));
        List<ExpenseDTO> content = expensePage.getContent()
                .stream()
                .map(expenseMapper::fromExpenseToExpenseDTO)
                .toList();
        return new ExpensePage(content, expensePage.getNumber(), expensePage.getTotalPages(), expensePage.getTotalElements());
    }

    @Override
    public BigDecimal loadExpenseAvgForSpecificMonth(Long userId, int year, int month) {
        return null;
    }

    @Override
    public void saveExpense(User user, ExpenseDTO expenseDto) throws EntityNotFoundException {
        var expense = expenseMapper.fromExpenseDTOtoExpense(expenseDto);
        expense.setUser(user);
        try {
            expenseRepository.save(expense);
        } catch (DataIntegrityViolationException e) {
            throw new EntityNotFoundException("User with this Id doesn't exist");
        }
    }
}
