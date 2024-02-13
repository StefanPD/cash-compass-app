package com.financing.app.budget;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping("/users/{userId}/budgets")
    public ResponseEntity<List<BudgetDTO>> getBudgetsByUserId(@PathVariable Long userId) {
        var budgets = budgetService.fetchBudgetsById(userId);
        return ResponseEntity.ok(budgets);
    }
}
