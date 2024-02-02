package com.financing.app;

import com.financing.app.savings.goals.SavingsGoalDTO;
import com.financing.app.savings.goals.SavingsGoalMapper;
import com.financing.app.savings.goals.SavingsGoalRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class AppApplication {

    @Autowired
    public SavingsGoalRepository savingsGoalRepository;
    @Autowired
    private SavingsGoalMapper savingsGoalMapper;

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    // This is left for testing purposes
    @PostConstruct
    public void init() {
        List<SavingsGoalDTO> savingGoalsList = savingsGoalRepository.findAll()
                .stream()
                .map(savingsGoalMapper::fromSavingsGoalToSavingGoalsDTO)
                .toList();

        System.out.println("has" + savingGoalsList.toString());
    }

}
