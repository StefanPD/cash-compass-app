package com.financing.app.expenses;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.financing.app.adapter.expenses.out.persistence.Expense;
import com.financing.app.adapter.expenses.out.persistence.ExpenseRepository;
import com.financing.app.application.expenses.domain.model.ExpenseDTO;
import com.financing.app.application.expenses.port.in.ExpenseRequest;
import com.financing.app.application.auth.domain.service.AuthenticationUseCase;
import com.financing.app.adapter.auth.out.persistence.Token;
import com.financing.app.adapter.auth.out.persistence.TokenRepository;
import com.financing.app.adapter.user.out.User;
import com.financing.app.adapter.user.out.UserRepository;
import com.financing.app.utils.AuthenticationHelperTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ExpenseControllerTest {

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

    @BeforeEach
    void setUp() {
        mapper.registerModule(new JavaTimeModule());
        var authenticationHelperTest = new AuthenticationHelperTest(authenticationUseCase, tokenRepository);
        token = authenticationHelperTest.registerUserTest();
        var user = userRepository.findByUsername("test123");
        if (user.isPresent()) {
            var expense = new Expense(1L, BigDecimal.valueOf(100.00), "Groceries Expense", LocalDate.parse("2024-01-01"), "test", user.get());
            expenseRepository.save(expense);
        }
    }

    @Test
    void whenRequestingExpense_withValidUserId_returnsExpenses() throws Exception {

        // When
        var result = mockMvc.perform(get("/api/v1/expenses")
                        .header("Authorization", "Bearer " + token.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonContent = result.getResponse().getContentAsString();

        List<ExpenseDTO> expenses = mapper.readValue(jsonContent, new TypeReference<>() {
        });

        // Then
        assertThat(expenses).isNotEmpty();
        assertThat(expenses.getFirst().getExpenseDate()).isEqualTo(LocalDate.parse("2024-01-01"));
        assertThat(expenses.getFirst().getAmount().compareTo(BigDecimal.valueOf(100.00))).isEqualTo(0);
        assertThat(expenses.getFirst().getCategory()).isEqualTo("Groceries Expense");
        assertThat(expenses.getFirst().getDescription()).isEqualTo("test");
    }

    @Test
    void whenUploadingExpense_withValidExpense_returnsSuccess_and_saveExpense() throws Exception {
        ExpenseRequest expenseRequest = new ExpenseRequest(
                BigDecimal.valueOf(100.00),
                "Food",
                LocalDate.now(),
                "Lunch with friends"
        );

        String expenseRequestJson = mapper.writeValueAsString(expenseRequest);

        // When
        mockMvc.perform(post("/api/v1/expenses")
                        .header("Authorization", "Bearer " + token.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expenseRequestJson))
                .andExpect(status().isNoContent());

        // Then
        var expense = expenseRepository.findExpensesByUser(new User(1L));

        assertThat(expense.size()).isEqualTo(2);
        assertThat(expense.get(1).category()).isEqualTo("Food");
    }

    @Test
    void whenUploadingExpense_withInvalidExpense_returnsError() throws Exception {
        ExpenseRequest expenseRequest = new ExpenseRequest(
                BigDecimal.valueOf(-100),
                "",
                LocalDate.now(),
                "Lunch with friends"
        );

        String expenseRequestJson = mapper.writeValueAsString(expenseRequest);

        mockMvc.perform(post("/api/v1/expenses")
                        .header("Authorization", "Bearer " + token.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expenseRequestJson))
                .andExpect(status().isInternalServerError());
    }
}
