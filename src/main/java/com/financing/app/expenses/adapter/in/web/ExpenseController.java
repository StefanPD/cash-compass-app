package com.financing.app.expenses.adapter.in.web;

import com.financing.app.bootstrap_module.utils.ApiVersion;
import com.financing.app.expenses.application.domain.model.ExpenseDTO;
import com.financing.app.expenses.application.domain.service.ExpenseUseCase;
import com.financing.app.expenses.application.port.in.*;
import com.financing.app.income.application.domain.model.IncomeDTO;
import com.financing.app.income.application.port.in.IncomeUpdateRequest;
import com.financing.app.user.application.domain.service.UserIdentityProviderUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
@ApiVersion("api/v1/expenses")
@Slf4j
@AllArgsConstructor
public class ExpenseController {
    private final ExpenseUseCase expenseUseCase;
    private final UserIdentityProviderUseCase userIdentityService;

    @GetMapping
    public ResponseEntity<List<ExpenseInfo>> getExpensesByUserId() {
        var user = userIdentityService.getAuthenticatedUser();
        log.info("GET request received - api/v1/expenses, for userId: {} ", user.getUserId());
        return ResponseEntity.ok(expenseUseCase.fetchExpensesByUserId(user.getUserId()));
    }

    @PostMapping
    public ResponseEntity<Void> postExpense(@Valid @RequestBody ExpenseRequest expenseRequest) {
        var user = userIdentityService.getAuthenticatedUser();

        log.info("POST request received - api/v1/expenses, for userId: {} and expense: {}", user.getUserId(), expenseRequest);
        var expenseDto = new ExpenseDTO(
                expenseRequest.amount(),
                expenseRequest.category(),
                expenseRequest.expenseDate(),
                expenseRequest.description()
        );
        expenseUseCase.saveExpense(user.getUserId(), expenseDto);
        log.info("Expense saved - api/v1/expenses, for userId: {} and expense: {}", user.getUserId(), expenseDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/history")
    public ResponseEntity<ExpensePage> getExpenseHistory(@RequestParam("date") LocalDate date,
                                                         @RequestParam("size") int size,
                                                         @RequestParam("page") int page) {
        var user = userIdentityService.getAuthenticatedUser();
        log.info("GET request received - api/v1/expenses/history, for userId: {} ", user.getUserId());
        var expenses = expenseUseCase.fetchExpensesForSpecificMonth(user.getUserId(), date, size, page);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/monthly-overview")
    public ResponseEntity<MonthlyOverview> getMonthlyOverview(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        var user = userIdentityService.getAuthenticatedUser();
        log.info("GET request received - api/v1/expenses/monthly-overview, for userId: {}", user.getUserId());
        var monthlyOverview = expenseUseCase.fetchMonthlyOverview(user, date);
        return ResponseEntity.ok(monthlyOverview);
    }

    @PutMapping
    public ResponseEntity<Void> updateExpense(@Valid @RequestBody ExpenseUpdateRequest expenseUpdateRequest){
        var user = userIdentityService.getAuthenticatedUser();
        log.info("PUT request received - api/v1/expenses, for userId: {}, expense: {}", user.getUserId(), expenseUpdateRequest);
        var expenseDto = new ExpenseDTO(expenseUpdateRequest);
        expenseUseCase.updateExpense(user, expenseDto);
        log.info("Expense updated - api/v1/expenses, for userId: {}, expense: {}", user.getUserId(), expenseUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteExpense(@Valid @RequestParam Long expenseId) {
        var user = userIdentityService.getAuthenticatedUser();
        log.info("Received DELETE request - api/v1/expense, for userId: {}", user.getUserId());
        expenseUseCase.deleteExpense(user, expenseId);
        return ResponseEntity.noContent().build();
    }
}