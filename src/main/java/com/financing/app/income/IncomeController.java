package com.financing.app.income;

import com.financing.app.utils.ApiVersion;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@ApiVersion("api/v1/income")
@Slf4j
@AllArgsConstructor
public class IncomeController {
    private final IncomeService incomeService;
    private final IncomeDateTransformer dateTransformer;

    @GetMapping("{userId}/incomes")
    public ResponseEntity<List<IncomeInfo>> getIncomesByUserId(@PathVariable @Min(1) Long userId) {
        log.info("GET request received - api/v1/income/{userId}/incomes, for userId: {}", userId);
        var incomes = incomeService.fetchIncomesByUserId(userId);
        return ResponseEntity.ok(incomes);
    }

    @GetMapping("{userId}/history")
    public ResponseEntity<Map<Integer, Map<String, List<IncomeDTO>>>> getIncomesByUserId(@PathVariable @Min(1) Long userId,
                                                                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        log.info("GET request received - api/v1/income/{userId}/history, for userId: {}, startDate: {}, endDate: {}", userId, startDate, endDate);
        if (startDate == null && endDate == null) {
            endDate = LocalDate.now();
            startDate = endDate.minusMonths(1);
        } else if (startDate == null) {
            startDate = endDate.minusMonths(1);
        } else if (endDate == null) {
            endDate = startDate.plusMonths(1);
        }
        if (startDate.isAfter(endDate)) {
            log.error("GET request failure - api/v1/income/{userId}/history, for userId: {}, startDate: {}, endDate: {}", userId, startDate, endDate);
            throw new IllegalArgumentException("Start date must not come after end date.");
        }
        var history = incomeService.fetchIncomesByHistory(userId, startDate, endDate);
        var response = dateTransformer.transform(history);
        return ResponseEntity.ok(response);
    }

    @PostMapping("{userId}/income")
    public ResponseEntity<Void> postIncome(@PathVariable @Min(1) Long userId,
                                           @Valid @RequestBody IncomeRequest incomeRequest) {
        log.info("POST request received - api/v1/income/{userId}/income, for userId: {}, income: {}", userId, incomeRequest);
        var incomeDto = new IncomeDTO(
                incomeRequest.amount(),
                incomeRequest.source(),
                incomeRequest.incomeDate(),
                incomeRequest.description()
        );
        incomeService.saveIncome(userId, incomeDto);
        log.info("Income saved - api/v1/income/{userId}/income, for userId: {}, income: {}", userId, incomeRequest);
        return ResponseEntity.noContent().build();
    }
}