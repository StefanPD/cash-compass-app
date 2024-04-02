package com.financing.app.savings.goals;

import com.financing.app.user.UserIdentityService;
import com.financing.app.utils.ApiVersion;
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

    private final SavingsGoalsService savingsGoalsService;
    private final UserIdentityService userIdentityService;

    @GetMapping
    ResponseEntity<List<SavingGoalInfo>> getSavingsGoalsByUserId() {
        var user = userIdentityService.getAuthenticatedUser();
        log.info("GET request received - api/v1/saving-goals, for userId: {}", user.getUserId());
        return ResponseEntity.ok(savingsGoalsService.fetchSavingsGoalsByUserId(user.getUserId()));
    }

    @PutMapping
    ResponseEntity<Void> updateSavingsGoalsByUserId(@Validated @RequestBody SavingsGoalUpdateRequest request) {
        var user = userIdentityService.getAuthenticatedUser();
        log.info("Received PUT request - api/v1/saving-goals, for userId: {}", user.getUserId());
        if (request.getEndDate().isBefore(request.getStartDate())) {
            log.error("Received PUT request failure - api/v1/saving-goals, for userId: {}", user.getUserId());
            throw new IllegalArgumentException("End date cannot precede start date.");
        }
        var savingsGoal = new SavingsGoalDTO(
                request.getSavingGoalId(),
                request.getName(),
                request.getTargetAmount(),
                request.getCurrentAmount(),
                request.getStartDate(),
                request.getEndDate(),
                user
        );
        savingsGoalsService.updateSavingsGoal(savingsGoal);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    ResponseEntity<Void> deleteSavingsGoalsByUserId(@RequestParam Long savingsGoalId) {
        var user = userIdentityService.getAuthenticatedUser();
        log.info("Received DELETE request - api/v1/saving-goals, for userId: {}", user.getUserId());
        savingsGoalsService.deleteSavingsGoalWithIds(user.getUserId(), savingsGoalId);
        return ResponseEntity.noContent().build();
    }
}
