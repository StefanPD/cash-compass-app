package com.financing.app.expenses;

import com.financing.app.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
}
