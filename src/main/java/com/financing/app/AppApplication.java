package com.financing.app;

import com.financing.app.budget.BudgetMapper;
import com.financing.app.budget.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppApplication {

    @Autowired
    public BudgetRepository budgetRepository;

    @Autowired
    private BudgetMapper budgetMapper;

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    // This is left for testing purposes
//    @PostConstruct
//    public void init() {
//        List<BudgetDTO> budgetList = budgetRepository.findAll()
//                .stream()
//                .map(budgetMapper::fromBudgetToBudgetDTO)
//                .toList();
//
//        System.out.println("has" + budgetList.toString());
//    }

}
