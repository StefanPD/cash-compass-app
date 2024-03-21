package com.financing.app.budget;

import com.financing.app.utils.ApiVersion;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
@ApiVersion("api/v1/budget")
@Slf4j
@AllArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @GetMapping("{userId}/budgets")
    public ResponseEntity<List<BudgetInfo>> getBudgetsByUserId(@PathVariable @Min(1) Long userId) {
        log.info("GET request received - api/v1/budget/{userId}/budgets, for userId: {}", userId);
        var budgets = budgetService.fetchBudgetsById(userId);
        return ResponseEntity.ok(budgets);
    }

    @GetMapping("{userId}/budget-expense-check")
    public ResponseEntity<BudgetExpensesDiff> getBudgetVsExpenseTotal(@PathVariable @Min(1) Long userId,
                                                                      @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        log.info("GET request received - api/v1/budget/{userId}/budget-expense-check, for userId: {} and date: {}", userId, date);
        if (date.isAfter(LocalDate.now())) {
            log.error("Received GET request failure - api/v1/budget/{userId}/budget-expense-check, for userId: {} and date: {}", userId, date);
            throw new IllegalArgumentException("Date cannot be in the future");
        }
        log.info("GET request received - api/v1/budget/{userId}/budget-expense-check, for userId: {} and date: {}", userId, date);
        var budgetVsExpense = budgetService.fetchBudgetsVsExpense(userId, date);
        return ResponseEntity.ok(budgetVsExpense);
    }
}