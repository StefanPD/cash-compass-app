package com.financing.app.budget;

import com.financing.app.user.UserIdentityService;
import com.financing.app.utils.ApiVersion;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
@ApiVersion("api/v1/budgets")
@Slf4j
@AllArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;
    private final UserIdentityService userIdentityService;

    @GetMapping
    public ResponseEntity<List<BudgetInfo>> getBudgetsByUserId() {
        var user = userIdentityService.getAuthenticatedUser();
        log.info("GET request received - api/v1/budget, for userId: {}", user.getUserId());
        var budgets = budgetService.fetchBudgetsById(user.getUserId());
        return ResponseEntity.ok(budgets);
    }

    @GetMapping("/budget-expense-check")
    public ResponseEntity<BudgetExpensesDiff> getBudgetVsExpenseTotal(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        var user = userIdentityService.getAuthenticatedUser();
        log.info("GET request received - api/v1/budget/budget-expense-check, for userId: {} and date: {}", user.getUserId(), date);
        if (date.isAfter(LocalDate.now())) {
            log.error("Received GET request failure - api/v1/budget/budget-expense-check, for userId: {} and date: {}", user.getUserId(), date);
            throw new IllegalArgumentException("Date cannot be in the future");
        }
        log.info("GET request received - api/v1/budget/budget-expense-check, for userId: {} and date: {}", user.getUserId(), date);
        var budgetVsExpense = budgetService.fetchBudgetsVsExpense(user.getUserId(), date);
        return ResponseEntity.ok(budgetVsExpense);
    }
}