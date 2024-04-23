package com.financing.app.savings_goals.adapter.in.web;

import com.financing.app.bootstrap_module.utils.ApiVersion;
import com.financing.app.savings_goals.application.domain.model.SavingsGoalDTO;
import com.financing.app.savings_goals.application.domain.service.SavingsGoalsUseCase;
import com.financing.app.savings_goals.application.port.in.SavingGoalInfo;
import com.financing.app.savings_goals.application.port.in.SavingsGoalPostRequest;
import com.financing.app.savings_goals.application.port.in.SavingsGoalUpdateRequest;
import com.financing.app.user.application.domain.service.UserIdentityProviderUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@ApiVersion("api/v1/saving-goals")
@Slf4j
@AllArgsConstructor
public class SavingsGoalsController {

    private final SavingsGoalsUseCase savingsGoalsUseCase;
    private final UserIdentityProviderUseCase userIdentityProviderUseCase;

    @GetMapping
    ResponseEntity<List<SavingGoalInfo>> getSavingsGoals() {
        var user = userIdentityProviderUseCase.getAuthenticatedUser();
        log.info("GET request received - api/v1/saving-goals, for userId: {}", user.getUserId());
        return ResponseEntity.ok(savingsGoalsUseCase.fetchSavingsGoalsByUserId(user.getUserId()));
    }

    @PutMapping
    ResponseEntity<Void> updateSavingsGoals(@Validated @RequestBody SavingsGoalUpdateRequest savingsGoalUpdateRequest) {
        var user = userIdentityProviderUseCase.getAuthenticatedUser();
        log.info("Received PUT request - api/v1/saving-goals, for userId: {}", user.getUserId());
        if (savingsGoalUpdateRequest.getEndDate().isBefore(savingsGoalUpdateRequest.getStartDate())) {
            log.error("Received PUT request failure - api/v1/saving-goals, for userId: {}", user.getUserId());
            throw new IllegalArgumentException("End date cannot precede start date.");
        }
        var savingsGoal = new SavingsGoalDTO(savingsGoalUpdateRequest);
        savingsGoalsUseCase.updateSavingsGoal(user, savingsGoal);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    ResponseEntity<Void> deleteSavingsGoals(@RequestParam Long savingsGoalId) {
        var user = userIdentityProviderUseCase.getAuthenticatedUser();
        log.info("Received DELETE request - api/v1/saving-goals, for userId: {}", user.getUserId());
        savingsGoalsUseCase.deleteSavingsGoalWithIds(user.getUserId(), savingsGoalId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Void> postSavingsGoals(@Valid @RequestBody SavingsGoalPostRequest savingsGoalPostRequest) {
        var user = userIdentityProviderUseCase.getAuthenticatedUser();
        log.info("POST request received - api/v1/savings-goals, for userId: {} and savings-goals: {}", user.getUserId(), savingsGoalPostRequest);
        var savingGoalsDto = new SavingsGoalDTO(savingsGoalPostRequest);
        savingsGoalsUseCase.saveSavingsGoals(user, savingGoalsDto);
        log.info("Expense saved - api/v1/savings-goals, for userId: {} and savings-goals: {}", user.getUserId(), savingGoalsDto);
        return ResponseEntity.noContent().build();
    }
}
