package com.financing.app.income;

import com.financing.app.utils.ApiVersion;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@ApiVersion("api/v1")
public class IncomeController {
    private final IncomeService incomeService;
    private final IncomeDateTransformer dateTransformer;

    public IncomeController(IncomeService incomeService, IncomeDateTransformer dateTransformer) {
        this.incomeService = incomeService;
        this.dateTransformer = dateTransformer;
    }

    @GetMapping("users/{userId}/incomes")
    public ResponseEntity<List<IncomeDTO>> getIncomesByUserId(@PathVariable @Min(1) Long userId) {
        var incomes = incomeService.fetchIncomesByUserId(userId);
        return ResponseEntity.ok(incomes);
    }

    @GetMapping("incomes/{userId}/history")
    public ResponseEntity<Map<Integer, Map<String, List<IncomeDTO>>>> getIncomesByUserId(@PathVariable @Min(1) Long userId,
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
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must not come after end date.");
        }
        var history = incomeService.fetchIncomesByHistory(userId, startDate, endDate);
        var response = dateTransformer.transform(history);
        return ResponseEntity.ok(response);
    }

    @PostMapping("incomes/{userId}/income")
    public ResponseEntity<Void> postIncome(@PathVariable @Min(1) Long userId,
                                           @Valid @RequestBody IncomeRequest incomeRequest) {

        var incomeDto = new IncomeDTO(
                incomeRequest.amount(),
                incomeRequest.source(),
                incomeRequest.incomeDate(),
                incomeRequest.description()
        );
        incomeService.saveIncome(userId, incomeDto);
        return ResponseEntity.noContent().build();
    }
}