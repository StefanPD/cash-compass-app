package com.financing.app.expenses;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.financing.app.auth.adapter.out.persistence.Token;
import com.financing.app.auth.adapter.out.persistence.TokenRepository;
import com.financing.app.auth.application.domain.service.AuthenticationUseCase;
import com.financing.app.bootstrap_module.exception.ErrorResponse;
import com.financing.app.budget.adapter.out.persistence.Budget;
import com.financing.app.budget.adapter.out.persistence.BudgetRepository;
import com.financing.app.expenses.adapter.out.persistence.Expense;
import com.financing.app.expenses.adapter.out.persistence.ExpenseRepository;
import com.financing.app.expenses.application.domain.model.ExpenseDTO;
import com.financing.app.expenses.application.port.in.ExpenseRequest;
import com.financing.app.expenses.application.port.in.MonthlyOverview;
import com.financing.app.user.adapter.out.User;
import com.financing.app.user.adapter.out.UserRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {com.financing.app.bootstrap_module.AppApplication.class})
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

    @Autowired
    private BudgetRepository budgetRepository;

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
            var budget = new Budget(1L, BigDecimal.valueOf(100.00), 1, 2024, user.get());
            expenseRepository.save(expense);
            budgetRepository.save(budget);
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

    @Test
    void whenRequestingMonthlyOverview_withValidDate_returnsSuccessMonthlyOverview() throws Exception {
        // When
        var date = LocalDate.of(2024, 1, 1);
        var result = mockMvc.perform(get("/api/v1/expenses/monthly-overview")
                        .header("Authorization", "Bearer " + token.getToken())
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonContent = result.getResponse().getContentAsString();

        MonthlyOverview monthlyOverview = mapper.readValue(jsonContent, MonthlyOverview.class);

        // Then
        assertThat(monthlyOverview.budgetInfo()).isNotNull();
        assertThat(monthlyOverview.expenses()).isNotEmpty();
        assertThat(monthlyOverview.expenses().size()).isEqualTo(1);
    }

    @Test
    void whenRequestingMonthlyOverview_withInvalidDate_returnsError() throws Exception {
        // When
        var date = "01-01-1111";
        var result = mockMvc.perform(get("/api/v1/expenses/monthly-overview")
                        .header("Authorization", "Bearer " + token.getToken())
                        .param("date", date))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonContent = result.getResponse().getContentAsString();
        var error = mapper.readValue(jsonContent, ErrorResponse.class);
        // Then
        assertThat(error).isInstanceOf(ErrorResponse.class);
        assertThat(error.status()).isEqualTo(500);
        assertThat(error.error()).isNotBlank();
        assertThat(error.message()).isNotBlank();
    }
}
