package com.financing.app;

import com.financing.app.income.IncomeDTO;
import com.financing.app.income.IncomeMapper;
import com.financing.app.income.IncomeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class AppApplication {

    @Autowired
    public IncomeRepository incomeRepository;
    @Autowired
    private IncomeMapper incomeMapper;
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    // This is left for testing purposes
    @PostConstruct
    public void init() {
        List<IncomeDTO> incomeList = incomeRepository.findAll()
                .stream()
                .map(incomeMapper::fromIncomeToIncomeDTO)
                .toList();

        System.out.println("has" + incomeList.toString());
    }

}
