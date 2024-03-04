package com.financing.app.savings.goals;

import com.financing.app.utils.ApiVersion;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@ApiVersion("api/v1")
public class SavingsGoalsController {

    private final SavingsGoalsService savingsGoalsService;

    public SavingsGoalsController(SavingsGoalsService savingsGoalsService) {
        this.savingsGoalsService = savingsGoalsService;
    }

    @GetMapping("/users/{userId}/savings-goals")
    ResponseEntity<List<SavingsGoalDTO>> getSavingsGoalsByUserId(@PathVariable("userId") @Min(1) Long userId) {
        return ResponseEntity.ok(savingsGoalsService.fetchSavingsGoalsByUserId(userId));
    }
}
