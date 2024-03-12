package com.financing.app.savings.goals;

import com.financing.app.utils.ApiVersion;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@ApiVersion("api/v1/saving-goal")
@Slf4j
public class SavingsGoalsController {

    private final SavingsGoalsService savingsGoalsService;

    public SavingsGoalsController(SavingsGoalsService savingsGoalsService) {
        this.savingsGoalsService = savingsGoalsService;
    }

    @GetMapping("{userId}/savings-goals")
    ResponseEntity<List<SavingsGoalDTO>> getSavingsGoalsByUserId(@PathVariable("userId") @Min(1) Long userId) {
        log.info("GET request received - api/v1/saving-goal/{userId}/savings-goals, for userId: {}", userId);
        return ResponseEntity.ok(savingsGoalsService.fetchSavingsGoalsByUserId(userId));
    }
}
