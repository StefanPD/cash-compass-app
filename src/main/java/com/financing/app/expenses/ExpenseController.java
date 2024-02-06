package com.financing.app.expenses;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("users/{userId}/expenses")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(expenseService.fetchExpensesByUserId(userId));
    }
}
