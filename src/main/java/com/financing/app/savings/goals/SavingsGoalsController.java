package com.financing.app.savings.goals;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SavingsGoalsController {

    private final SavingsGoalsService savingsGoalsService;

    public SavingsGoalsController(SavingsGoalsService savingsGoalsService) {
        this.savingsGoalsService = savingsGoalsService;
    }

    @GetMapping("/users/{userId}/savings-goals")
    ResponseEntity<List<SavingsGoalDTO>> getSavingsGoalsByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(savingsGoalsService.fetchSavingsGoalsByUserId(userId));
    }
}
