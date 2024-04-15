package com.financing.app.income.adapter.in.web;

import com.financing.app.income.application.domain.model.IncomeDTO;
import com.financing.app.income.application.domain.sevice.IncomeUseCase;
import com.financing.app.income.application.port.in.IncomeDateTransformer;
import com.financing.app.income.application.port.in.IncomeInfo;
import com.financing.app.income.application.port.in.IncomeRequest;
import com.financing.app.user.application.domain.service.UserIdentityProviderUseCase;
import com.financing.app.bootstrap_module.utils.ApiVersion;
import jakarta.validation.Valid;
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
@ApiVersion("api/v1/incomes")
@Slf4j
@AllArgsConstructor
public class IncomeController {
    private final IncomeUseCase incomeUseCase;
    private final IncomeDateTransformer dateTransformer;
    private final UserIdentityProviderUseCase userIdentityProviderUseCase;

    @GetMapping
    public ResponseEntity<List<IncomeInfo>> getIncomesByUserId() {
        var user = userIdentityProviderUseCase.getAuthenticatedUser();
        log.info("GET request received - api/v1/incomes, for userId: {}", user.getUserId());
        var incomes = incomeUseCase.fetchIncomesByUserId(user.getUserId());
        return ResponseEntity.ok(incomes);
    }

    @GetMapping("/history")
    public ResponseEntity<Map<Integer, Map<String, List<IncomeDTO>>>> getIncomesByUserId(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        var user = userIdentityProviderUseCase.getAuthenticatedUser();
        log.info("GET request received - api/v1/incomes/history, for userId: {}, startDate: {}, endDate: {}", user.getUserId(), startDate, endDate);
        if (startDate == null && endDate == null) {
            endDate = LocalDate.now();
            startDate = endDate.minusMonths(1);
        } else if (startDate == null) {
            startDate = endDate.minusMonths(1);
        } else if (endDate == null) {
            endDate = startDate.plusMonths(1);
        }
        if (startDate.isAfter(endDate)) {
            log.error("GET request failure - api/v1/incomes/history, for userId: {}, startDate: {}, endDate: {}", user.getUserId(), startDate, endDate);
            throw new IllegalArgumentException("Start date must not come after end date.");
        }
        var history = incomeUseCase.fetchIncomesByHistory(user.getUserId(), startDate, endDate);
        var response = dateTransformer.transform(history);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> postIncome(@Valid @RequestBody IncomeRequest incomeRequest) {
        var user = userIdentityProviderUseCase.getAuthenticatedUser();
        log.info("POST request received - api/v1/incomes, for userId: {}, income: {}", user.getUserId(), incomeRequest);
        var incomeDto = new IncomeDTO(
                incomeRequest.amount(),
                incomeRequest.source(),
                incomeRequest.incomeDate(),
                incomeRequest.description()
        );
        incomeUseCase.saveIncome(user.getUserId(), incomeDto);
        log.info("Income saved - api/v1/incomes, for userId: {}, income: {}", user.getUserId(), incomeRequest);
        return ResponseEntity.noContent().build();
    }
}