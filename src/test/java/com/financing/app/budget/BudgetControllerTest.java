package com.financing.app.budget;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.financing.app.exception.ErrorResponse;
import com.financing.app.expenses.Expense;
import com.financing.app.expenses.ExpenseRepository;
import com.financing.app.user.User;
import com.financing.app.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
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

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mapper.registerModule(new JavaTimeModule());
        var user = new User(1L, "test@email.com", "test", "test123", LocalDateTime.now(), LocalDateTime.now());
        var budget = new Budget(1L, BigDecimal.valueOf(100.00), 1, 2024, user);
        var expense = new Expense(1L, BigDecimal.valueOf(100.00), "Groceries Expense", LocalDate.parse("2024-01-01"), "test", user);

        userRepository.save(user);
        budgetRepository.save(budget);
        expenseRepository.save(expense);
    }


    @Test
    void whenRequestingBudgets_withValidUserId_returnsBudgetDetails() throws Exception {
        // Given
        Long userId = 1L;

        // When
        var result = mockMvc.perform(get("/api/v1/budget/{userId}/budgets", userId))
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
    void whenRequestingBudgets_withInvalidUserId_throwsError() throws Exception {
        // Given
        Long userId = 0L;

        // When
        var result = mockMvc.perform(get("/api/v1/budget/{userId}", userId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonContent = result.getResponse().getContentAsString();

        ErrorResponse error = mapper.readValue(jsonContent, ErrorResponse.class);

        // Then
        assertThat(error).isInstanceOf(ErrorResponse.class);
        assertThat(error.status()).isEqualTo(500);
        assertThat(error.error()).isNotBlank();
        assertThat(error.message()).isNotBlank();
    }

    @Test
    @Disabled("Temporarily disabled due to H2-PostgreSQL compatibility issues affecting test accuracy. Awaiting a workaround or solution.")
    void whenRequestingBudgetsVsExpense_withValidUserId_returnsBudgetsVsExpense() throws JsonProcessingException, Exception {
        // Given
        Long userId = 1L;
        String date = "2024-01-01";

        //When
        var result = mockMvc.perform(get("/api/v1/budget/{userId}/budget-expense-check", userId)
                        .param("date", date))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonContent = result.getResponse().getContentAsString();

        var budgetVsExpenseDTO = mapper.readValue(jsonContent, BudgetVsExpenseDTO.class);

        // Then
        assertThat(budgetVsExpenseDTO.budget).isNotNull();
        assertThat(budgetVsExpenseDTO.expenses).isNotNull();
        assertThat(budgetVsExpenseDTO.expenses).isNotEmpty();
        assertThat(budgetVsExpenseDTO.diff).isNotNull();
    }

    @Test
    @Disabled("Temporarily disabled due to H2-PostgreSQL compatibility issues affecting test accuracy. Awaiting a workaround or solution.")
    void whenRequestingBudgetsVsExpense_withInvalidUserId_throwsError() throws JsonProcessingException {
    }

    @Test
    @Disabled("Temporarily disabled due to H2-PostgreSQL compatibility issues affecting test accuracy. Awaiting a workaround or solution.")
    void whenRequestingBudgetsVsExpense_withStartDateAfterEndDate_throwsError() throws JsonProcessingException {
    }
}