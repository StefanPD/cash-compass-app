package com.financing.app.income;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;

@RestController
public class IncomeController {

    private final IncomeService incomeService;
    private final IncomeDateTransformer dateTransformer;

    public IncomeController(IncomeService incomeService, IncomeDateTransformer dateTransformer) {
        this.incomeService = incomeService;
        this.dateTransformer = dateTransformer;
    }

    @GetMapping("users/{userId}/incomes")
    public ResponseEntity<List<IncomeDTO>> getIncomesByUserId(@PathVariable Long userId) {
        var incomes = incomeService.fetchIncomesByUserId(userId);
        return ResponseEntity.ok(incomes);
    }

    @GetMapping("incomes/{userId}/history")
    public ResponseEntity<Map<Integer, Map<String, List<IncomeDTO>>>> getIncomesByUserId(@PathVariable Long userId,
                                                                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate == null && endDate == null) {
            endDate = LocalDate.now();
            startDate = endDate.minusMonths(1);
        } else if (startDate == null) {
            startDate = endDate.minusMonths(1);
        } else if (endDate == null) {
            endDate = startDate.plusMonths(1);
        }
        var history = incomeService.fetchIncomesByHistory(userId, startDate, endDate);
        var response = dateTransformer.transform(history);
        return ResponseEntity.ok(response);
    }
}