package com.financing.app.budget;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping("/budgets/{userId}")
    public ResponseEntity<List<BudgetDTO>> getBudgetsByUserId(@PathVariable Long userId) {
        var budgets = budgetService.fetchBudgetsById(userId);
        return ResponseEntity.ok(budgets);
    }

    @GetMapping("budgets/{userId}/budget-expense-check")
    public ResponseEntity<BudgetVsExpenseDTO> getBudgetVsExpenseTotal(@PathVariable Long userId,
                                                                      @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        var budgetVsExpense = budgetService.fetchBudgetsVsExpense(userId, date);
        return ResponseEntity.ok(budgetVsExpense);
    }
}
