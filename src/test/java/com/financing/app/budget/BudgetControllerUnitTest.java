package com.financing.app.budget;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financing.app.user.User;
import com.financing.app.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BudgetControllerUnitTest {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        var user = new User(1L, "test@email.com", "test", "test123", LocalDateTime.now(), LocalDateTime.now());
        var budget = new Budget(1L, BigDecimal.valueOf(100.00), 1, 2024, user);

        userRepository.save(user);
        budgetRepository.save(budget);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void whenRequestingBudgets_withValidUserId_returnsBudgetDetails() throws Exception {
        Long userId = 1L;

        var result = mockMvc.perform(get("/budgets/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonContent = result.getResponse().getContentAsString();

        List<Budget> budgets = mapper.readValue(jsonContent, new TypeReference<>() {
        });

        assertThat(budgets).isNotEmpty();
        assertThat(budgets.getFirst().getTotalBudget().compareTo(BigDecimal.valueOf(100.00))).isEqualTo(0);
        assertThat(budgets.getFirst().getMonth()).isEqualTo(1);
        assertThat(budgets.getFirst().getYear()).isEqualTo(2024);
        assertThat(budgets.getFirst().getBudgetId()).isEqualTo(1L);
    }

    @Test
    void whenRequestingBudgets_withInvalidUserId_throwsError() throws JsonProcessingException {
    }

    @Test
    void whenRequestingBudgetsVsExpense_withValidUserId_returnsBudgetsVsExpense() throws JsonProcessingException {
    }

    @Test
    void whenRequestingBudgetsVsExpense_withInvalidUserId_throwsError() throws JsonProcessingException {
    }

    @Test
    void whenRequestingBudgetsVsExpense_withStartDateAfterEndDate_throwsError() throws JsonProcessingException {
    }
}