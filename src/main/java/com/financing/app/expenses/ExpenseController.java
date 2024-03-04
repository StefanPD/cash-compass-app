package com.financing.app.expenses;

import com.financing.app.income.IncomeDTO;
import com.financing.app.income.IncomeRequest;
import com.financing.app.utils.ApiVersion;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@ApiVersion("api/v1")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("users/{userId}/expenses")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByUserId(@PathVariable @Min(1) Long userId) {
        return ResponseEntity.ok(expenseService.fetchExpensesByUserId(userId));
    }

    @PostMapping("expenses/{userId}/expense")
    public ResponseEntity<Void> postExpense(@PathVariable @Min(1) Long userId,
                                            @Valid @RequestBody ExpenseRequest expenseRequest) {
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