package com.financing.app.expenses;

import com.financing.app.income.IncomeDTO;
import com.financing.app.income.IncomeRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("expenses/{userId}/expense")
    public ResponseEntity<Void> postExpense(@PathVariable Long userId,
                                           @RequestBody ExpenseRequest expenseRequest) {
        var expenseDto = new ExpenseDTO(
                expenseRequest.amount(),
                expenseRequest.category(),
                expenseRequest.expenseDate(),
                expenseRequest.description()
        );
        expenseService.saveExpense(userId, expenseDto);
        return ResponseEntity.noContent().build();
    }
}
