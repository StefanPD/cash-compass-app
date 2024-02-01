package com.financing.app;

import com.financing.app.expenses.ExpenseDTO;
import com.financing.app.expenses.ExpenseMapper;
import com.financing.app.expenses.ExpenseRepository;
import com.financing.app.user.User;
import com.financing.app.user.UserDTO;
import com.financing.app.user.UserMapper;
import com.financing.app.user.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class AppApplication {

    @Autowired
    public ExpenseRepository expenseRepository;
    @Autowired
    private ExpenseMapper expenseMapper;
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    // This is left for testing purposes
    @PostConstruct
    public void init() {

        List<ExpenseDTO> expenseList = expenseRepository.findAll()
                .stream()
                .map(expenseMapper::fromExpenseToExpenseDTO)
                .toList();
        System.out.println("has" + expenseList.toString());
    }

}
