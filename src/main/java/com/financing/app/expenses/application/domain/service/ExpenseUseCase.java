package com.financing.app.expenses.application.domain.service;

import com.financing.app.budget.application.port.in.BudgetInfo;
import com.financing.app.expenses.application.domain.model.ExpenseDTO;
import com.financing.app.expenses.application.port.in.ExpenseInfo;
import com.financing.app.expenses.application.port.in.ExpensePage;
import com.financing.app.expenses.application.port.in.MonthlyOverview;
import com.financing.app.expenses.application.port.out.LoadBudgetPortInterface;
import com.financing.app.expenses.application.port.out.LoadExpensePort;
import com.financing.app.income.application.domain.model.IncomeDTO;
import com.financing.app.user.adapter.out.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class ExpenseUseCase {
    private final LoadExpensePort loadExpensePort;

    private final LoadBudgetPortInterface loadBudgetPortInterface;

    public ExpenseUseCase(LoadExpensePort loadExpensePort, @Qualifier("loadBudgetPortInterface") LoadBudgetPortInterface loadBudgetPortInterface) {
        this.loadExpensePort = loadExpensePort;
        this.loadBudgetPortInterface = loadBudgetPortInterface;
    }

    @Transactional
    public List<ExpenseInfo> fetchExpensesByUserId(Long userId) {
        return loadExpensePort.loadExpenses(new User(userId));

    }

    @Transactional
    public void saveExpense(Long userId, ExpenseDTO expenseDto) throws EntityNotFoundException {
        loadExpensePort.saveExpense(new User(userId), expenseDto);
    }

    @Transactional
    public ExpensePage fetchExpensesForSpecificMonth(Long userId, LocalDate date, int size, int page) {
        return loadExpensePort.loadExpenseAtYearAndMonth(userId, date, size, page);
    }

    @Transactional
    public MonthlyOverview fetchMonthlyOverview(User user, LocalDate date) {
        var expenses = loadExpensePort.loadMonthlyOverview(user, date);
        var year = date.getYear();
        var month = date.getMonthValue();
        var budgetDTO = loadBudgetPortInterface.loadBudgetByDate(user, year, month);
        var budgetInfo = new BudgetInfo(
                budgetDTO.getTotalBudget(),
                budgetDTO.getMonth(),
                budgetDTO.getYear());
        return new MonthlyOverview(budgetInfo, expenses);
    }

    @Transactional
    public void deleteExpense(User user, Long expenseId){
        loadExpensePort.deleteExpense(user, expenseId);
    }

    @Transactional
    public void updateExpense(User user, ExpenseDTO expenseDTO) {
        loadExpensePort.updateExpense(user, expenseDTO);
    }
}
