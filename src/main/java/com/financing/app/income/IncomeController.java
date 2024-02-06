package com.financing.app.income;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @GetMapping("users/{userId}/incomes")
    public ResponseEntity<List<IncomeDTO>> getIncomesByUserId(@PathVariable Long userId) {
        var incomes = incomeService.fetchIncomesByUserId(userId);
        return ResponseEntity.ok(incomes);
    }
}
