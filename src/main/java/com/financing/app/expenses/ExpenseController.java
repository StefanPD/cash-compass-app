package com.financing.app.expenses;

import com.financing.app.utils.ApiVersion;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
@ApiVersion("api/v1/expense")
@Slf4j
@AllArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @GetMapping("{userId}/expenses")
    public ResponseEntity<List<ExpenseInfo>> getExpensesByUserId(@PathVariable @Min(1) Long userId) {
        log.info("GET request received - api/v1/expense/{userId}/expenses, for userId: {} ", userId);
        return ResponseEntity.ok(expenseService.fetchExpensesByUserId(userId));
    }

    @PostMapping("{userId}/expense")
    public ResponseEntity<Void> postExpense(@PathVariable @Min(1) Long userId,
                                            @Valid @RequestBody ExpenseRequest expenseRequest) {
        log.info("POST request received - api/v1/expense/{userId}/expense, for userId: {} and expense: {}", userId, expenseRequest);
        var expenseDto = new ExpenseDTO(
                expenseRequest.amount(),
                expenseRequest.category(),
                expenseRequest.expenseDate(),
                expenseRequest.description()
        );
        expenseService.saveExpense(userId, expenseDto);
        log.info("Expense saved - api/v1/expense/{userId}/expenses, for userId: {} and expense: {}", userId, expenseDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{userId}/history")
    public ResponseEntity<ExpensePage> getExpenseHistory(@PathVariable @Min(1) Long userId,
                                                           @RequestParam("date") LocalDate date,
                                                           @RequestParam("size") int size,
                                                           @RequestParam("page") int page) {
        log.info("GET request received - api/v1/expense/{userId}/history, for userId: {} ", userId);
        var expenses = expenseService.fetchExpensesForSpecificMonth(userId, date, size, page);
        return ResponseEntity.ok(expenses);
    }
}