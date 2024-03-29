package com.financing.app.expenses;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.financing.app.exception.ErrorResponse;
import com.financing.app.user.Role;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mapper.registerModule(new JavaTimeModule());
        var user = new User(1L, "test@email.com", "test", "test123", LocalDateTime.now(), LocalDateTime.now(), Role.USER);
        var expense = new Expense(1L, BigDecimal.valueOf(100.00), "Groceries Expense", LocalDate.parse("2024-01-01"), "test", user);

        userRepository.save(user);
        expenseRepository.save(expense);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void whenRequestingExpense_withValidUserId_returnsExpenses() throws Exception {
        // Given
        Long userId = 1L;

        // When
        var result = mockMvc.perform(get("/api/v1/expense/{userId}/expenses", userId))
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
    void whenRequestingExpense_withInvalidUserId_returnsError() throws Exception {
        // Given
        Long userId = 0L;

        // When
        var result = mockMvc.perform(get("/api/v1/expense/{userId}/expenses", userId))
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
    void whenUploadingExpense_withValidExpense_returnsSuccess_and_saveExpense() throws Exception {
        // Given
        Long userId = 1L;

        ExpenseRequest expenseRequest = new ExpenseRequest(
                BigDecimal.valueOf(100.00),
                "Food",
                LocalDate.now(),
                "Lunch with friends"
        );

        String expenseRequestJson = mapper.writeValueAsString(expenseRequest);

        // When
        mockMvc.perform(post("/api/v1/expense/{userId}/expense", userId)
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
        // Given
        Long userId = 1L;

        ExpenseRequest expenseRequest = new ExpenseRequest(
                BigDecimal.valueOf(-100),
                "",
                LocalDate.now(),
                "Lunch with friends"
        );

        String expenseRequestJson = mapper.writeValueAsString(expenseRequest);

        mockMvc.perform(post("/api/v1/expense/{userId}/expense", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expenseRequestJson))
                .andExpect(status().isInternalServerError());
    }
}
