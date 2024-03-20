package com.financing.app.savings.goals;

import com.financing.app.user.User;
import com.financing.app.utils.ApiVersion;
import jakarta.validation.constraints.Min;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@ApiVersion("api/v1/saving-goal")
@Slf4j
@AllArgsConstructor
public class SavingsGoalsController {

    private final SavingsGoalsService savingsGoalsService;

    @GetMapping("{userId}/savings-goals")
    ResponseEntity<List<SavingsGoalDTO>> getSavingsGoalsByUserId(@PathVariable("userId") @Min(1) Long userId) {
        log.info("GET request received - api/v1/saving-goal/{userId}/savings-goals, for userId: {}", userId);
        return ResponseEntity.ok(savingsGoalsService.fetchSavingsGoalsByUserId(userId));
    }

    @PutMapping("{userId}/savings-goals")
    ResponseEntity<Void> updateSavingsGoalsByUserId(@PathVariable("userId") @Min(1) Long userId,
                                                    @Validated @RequestBody SavingsGoalUpdateRequest request) {
        log.info("Received PUT request - api/v1/saving-goal/{userId}/savings-goals, for userId: {}", userId);
        if (request.getEndDate().isBefore(request.getStartDate())) {
            log.error("Received PUT request failure - api/v1/saving-goal/{userId}/savings-goals, for userId: {}", userId);
            throw new IllegalArgumentException("End date cannot precede start date.");
        }
        var savingsGoal = new SavingsGoalDTO(
                request.getSavingGoalId(),
                request.getName(),
                request.getTargetAmount(),
                request.getCurrentAmount(),
                request.getStartDate(),
                request.getEndDate(),
                new User(userId)
        );
        savingsGoalsService.updateSavingsGoal(savingsGoal);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{userId}/savings-goals")
    ResponseEntity<Void> deleteSavingsGoalsByUserId(@PathVariable("userId") @Min(1) Long userId,
                                                    @RequestParam Long savingsGoalId) {
        log.info("Received DELETE request - api/v1/saving-goal/{userId}/savings-goals, for userId: {}", userId);
        savingsGoalsService.deleteSavingsGoalWithIds(userId, savingsGoalId);
        return ResponseEntity.noContent().build();
    }
}
