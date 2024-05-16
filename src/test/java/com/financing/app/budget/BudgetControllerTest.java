package com.financing.app.budget;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.financing.app.auth.adapter.out.persistence.Token;
import com.financing.app.auth.adapter.out.persistence.TokenRepository;
import com.financing.app.auth.application.domain.service.AuthenticationUseCase;
import com.financing.app.budget.adapter.out.persistence.Budget;
import com.financing.app.budget.adapter.out.persistence.BudgetRepository;
import com.financing.app.budget.application.port.in.BudgetExpensesDiff;
import com.financing.app.expenses.adapter.out.persistence.Expense;
import com.financing.app.expenses.adapter.out.persistence.ExpenseRepository;
import com.financing.app.user.adapter.out.UserRepository;
import com.financing.app.utils.AuthenticationHelperTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(classes = {com.financing.app.bootstrap_module.AppApplication.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BudgetControllerTest {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationUseCase authenticationUseCase;

    @Autowired
    private TokenRepository tokenRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    private Token token;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");
    
    @BeforeEach
    void setUp() {
        mapper.registerModule(new JavaTimeModule());
        var authenticationHelperTest = new AuthenticationHelperTest(authenticationUseCase, tokenRepository);
        token = authenticationHelperTest.registerUserTest();

        var user = userRepository.findByUsername("test123");
        if (user.isPresent()) {
            var budget = new Budget(1L, BigDecimal.valueOf(100.00), 1, 2024, user.get());
            var expense = new Expense(1L, BigDecimal.valueOf(100.00), "Groceries Expense", LocalDate.parse("2024-01-01"), "test", user.get());
            budgetRepository.save(budget);
            expenseRepository.save(expense);
        }
    }

    @Test
    void whenRequestingBudgets_withValidUserId_returnsBudgetDetails() throws Exception {

        // When
        var result = mockMvc.perform(get("/api/v1/budgets")
                        .header("Authorization", "Bearer " + token.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonContent = result.getResponse().getContentAsString();

        List<Budget> budgets = mapper.readValue(jsonContent, new TypeReference<>() {
        });

        // Then
        assertThat(budgets).isNotEmpty();
        assertThat(budgets.getFirst().getTotalBudget().compareTo(BigDecimal.valueOf(100.00))).isEqualTo(0);
        assertThat(budgets.getFirst().getMonth()).isEqualTo(1);
        assertThat(budgets.getFirst().getYear()).isEqualTo(2024);
    }

    @Test
    void whenRequestingBudgetsVsExpense_withValidUserId_returnsBudgetsVsExpense() throws Exception {
        var date = LocalDate.of(2024, 1, 1);
        // When
        var result = mockMvc.perform(get("/api/v1/budgets/budget-expense-check")
                        .header("Authorization", "Bearer " + token.getToken())
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        var json = result.getResponse().getContentAsString();
        BudgetExpensesDiff history = mapper.readValue(json, BudgetExpensesDiff.class);

        // Then
        assertThat(history).isNotNull();
        assertThat(history.getBudget()).isNotNull();
        assertThat(history.getDiff()).isNotNull();

    }

    @Test
    void whenRequestingBudgetsVsExpense_withStartDateAfterEndDate_throwsError() throws Exception {

        var date = LocalDate.of(2025, 1, 1);
        // When
        mockMvc.perform(get("/api/v1/budgets/budget-expense-check")
                        .header("Authorization", "Bearer " + token.getToken())
                        .param("date", date.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}