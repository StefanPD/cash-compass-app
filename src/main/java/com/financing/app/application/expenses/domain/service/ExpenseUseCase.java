package com.financing.app.application.expenses.domain.service;

import com.financing.app.adapter.user.out.User;
import com.financing.app.application.expenses.domain.model.ExpenseDTO;
import com.financing.app.application.expenses.port.in.ExpenseInfo;
import com.financing.app.application.expenses.port.in.ExpensePage;
import com.financing.app.application.expenses.port.out.LoadExpensePort;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ExpenseUseCase {
    private final LoadExpensePort loadExpensePort;

    public List<ExpenseInfo> fetchExpensesByUserId(Long userId) {
        return loadExpensePort.loadExpenses(new User(userId));

    }

    @Transactional
    public void saveExpense(Long userId, ExpenseDTO expenseDto) throws EntityNotFoundException {
        loadExpensePort.saveExpense(new User(userId), expenseDto);
    }

    public ExpensePage fetchExpensesForSpecificMonth(Long userId, LocalDate date, int size, int page) {
        return loadExpensePort.loadExpenseAtYearAndMonth(userId, date, size, page);
    }
}
