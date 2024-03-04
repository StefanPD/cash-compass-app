package com.financing.app.budget;

import com.financing.app.utils.ApiVersion;
import jakarta.validation.constraints.Min;
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
@ApiVersion("api/v1/budgets")
public class BudgetController {
    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping("{userId}")
    public ResponseEntity<List<BudgetDTO>> getBudgetsByUserId(@PathVariable @Min(1) Long userId) {
        var budgets = budgetService.fetchBudgetsById(userId);
        return ResponseEntity.ok(budgets);
    }

    @GetMapping("{userId}/budget-expense-check")
    public ResponseEntity<BudgetVsExpenseDTO> getBudgetVsExpenseTotal(@PathVariable @Min(1) Long userId,
                                                     @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the future");
        }
        var budgetVsExpense = budgetService.fetchBudgetsVsExpense(userId, date);
        return ResponseEntity.ok(budgetVsExpense);
    }
}