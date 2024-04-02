package com.financing.app.expenses;

import com.financing.app.user.UserIdentityService;
import com.financing.app.utils.ApiVersion;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
@ApiVersion("api/v1/expenses")
@Slf4j
@AllArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;
    private final UserIdentityService userIdentityService;

    @GetMapping
    public ResponseEntity<List<ExpenseInfo>> getExpensesByUserId() {
        var user = userIdentityService.getAuthenticatedUser();
        log.info("GET request received - api/v1/expenses, for userId: {} ", user.getUserId());
        return ResponseEntity.ok(expenseService.fetchExpensesByUserId(user.getUserId()));
    }

    @PostMapping
    public ResponseEntity<Void> postExpense(@Valid @RequestBody ExpenseRequest expenseRequest) {
        var user = userIdentityService.getAuthenticatedUser();

        log.info("POST request received - api/v1/expenses, for userId: {} and expense: {}", user.getUserId(), expenseRequest);
        var expenseDto = new ExpenseDTO(
                expenseRequest.amount(),
                expenseRequest.category(),
                expenseRequest.expenseDate(),
                expenseRequest.description()
        );
        expenseService.saveExpense(user.getUserId(), expenseDto);
        log.info("Expense saved - api/v1/expenses, for userId: {} and expense: {}", user.getUserId(), expenseDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/history")
    public ResponseEntity<ExpensePage> getExpenseHistory(@RequestParam("date") LocalDate date,
                                                         @RequestParam("size") int size,
                                                         @RequestParam("page") int page) {
        var user = userIdentityService.getAuthenticatedUser();
        log.info("GET request received - api/v1/expenses/history, for userId: {} ", user.getUserId());
        var expenses = expenseService.fetchExpensesForSpecificMonth(user.getUserId(), date, size, page);
        return ResponseEntity.ok(expenses);
    }
}