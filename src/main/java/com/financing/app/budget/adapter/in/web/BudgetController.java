package com.financing.app.budget.adapter.in.web;

import com.financing.app.bootstrap_module.utils.ApiVersion;
import com.financing.app.budget.application.domain.model.BudgetDTO;
import com.financing.app.budget.application.domain.service.BudgetUseCase;
import com.financing.app.budget.application.port.in.BudgetExpensesDiff;
import com.financing.app.budget.application.port.in.BudgetInfo;
import com.financing.app.budget.application.port.in.BudgetRequest;
import com.financing.app.budget.application.port.in.BudgetUpdateRequest;
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
@ApiVersion("api/v1/budgets")
@Slf4j
@AllArgsConstructor
public class BudgetController {
    private final BudgetUseCase budgetUseCase;
    private final UserIdentityProviderUseCase userIdentityProviderUseCase;

    @GetMapping
    public ResponseEntity<List<BudgetInfo>> getBudgetsByUserId() {
        var user = userIdentityProviderUseCase.getAuthenticatedUser();
        log.info("GET request received - api/v1/budgets, for userId: {}", user.getUserId());
        var budgets = budgetUseCase.fetchBudgetsById(user.getUserId());
        return ResponseEntity.ok(budgets);
    }

    @GetMapping("/budget-expense-check")
    public ResponseEntity<BudgetExpensesDiff> getBudgetVsExpenseTotal(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        var user = userIdentityProviderUseCase.getAuthenticatedUser();
        log.info("GET request received - api/v1/budgets/budget-expense-check, for userId: {} and date: {}", user.getUserId(), date);
        if (date.isAfter(LocalDate.now())) {
            log.error("Received GET request failure - api/v1/budgets/budget-expense-check, for userId: {} and date: {}", user.getUserId(), date);
            throw new IllegalArgumentException("Date cannot be in the future");
        }
        log.info("GET request received - api/v1/budgets/budget-expense-check, for userId: {} and date: {}", user.getUserId(), date);
        var budgetVsExpense = budgetUseCase.fetchBudgetsVsExpense(user.getUserId(), date);
        return ResponseEntity.ok(budgetVsExpense);
    }

    @PostMapping
    public ResponseEntity<Void> postBudget(@Valid @RequestBody BudgetRequest budgetRequest) {
        var user = userIdentityProviderUseCase.getAuthenticatedUser();
        log.info("POST request received - api/v1/budgets, for userId: {}", user.getUserId());
        var budget = new BudgetDTO(budgetRequest.totalBudget(), budgetRequest.month(), budgetRequest.year());
        budgetUseCase.saveBudget(user.getUserId(), budget);
        log.info("Budget saved - api/v1/budgets, for userId: {}, budget: {}", user.getUserId(), budgetRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBudget(@Valid @RequestParam Long budgetId) {
        var user = userIdentityProviderUseCase.getAuthenticatedUser();
        log.info("Received DELETE request - api/v1/budgets, for userId: {}", user.getUserId());
        budgetUseCase.deleteBudget(user.getUserId(), budgetId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateBudget(@Validated @RequestBody BudgetUpdateRequest request) {
        var user = userIdentityProviderUseCase.getAuthenticatedUser();
        log.info("Received PUT request - api/v1/budgets, for userId: {}", user.getUserId());
        var budget = new BudgetDTO(
                request.getBudgetId(),
                request.getTotalBudget(),
                request.getMonth(),
                request.getYear()
        );
        budgetUseCase.updateBudget(user.getUserId(), budget);
        return ResponseEntity.noContent().build();
    }
}