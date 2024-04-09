package com.financing.app.savings.goals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.financing.app.adapter.savings_goals.out.persistence.SavingsGoal;
import com.financing.app.adapter.savings_goals.out.persistence.SavingsGoalRepository;
import com.financing.app.application.savings_goals.port.in.SavingGoalInfo;
import com.financing.app.application.auth.domain.service.AuthenticationUseCase;
import com.financing.app.adapter.auth.out.persistence.Token;
import com.financing.app.adapter.auth.out.persistence.TokenRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SavingsGoalsControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SavingsGoalRepository savingsGoalRepository;

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
            var savingGoal = new SavingsGoal(1L, "vacation", BigDecimal.valueOf(1000.00), BigDecimal.valueOf(100.00), LocalDate.parse("2024-01-01"), LocalDate.parse("2024-06-01"), user.get());
            savingsGoalRepository.save(savingGoal);
        }
    }

    @Test
    void whenRequestingSavingGoals_withValidUserId_returnsSavingGoals() throws Exception {
        // When
        var result = mockMvc.perform(get("/api/v1/saving-goals")
                .header("Authorization", "Bearer " + token.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonContent = result.getResponse().getContentAsString();

        List<SavingGoalInfo> savingsGoals = mapper.readValue(jsonContent, new TypeReference<>() {
        });

        // Then
        assertThat(savingsGoals).isNotEmpty();
        assertThat(savingsGoals.getFirst().name()).isEqualTo("vacation");
        assertThat(savingsGoals.getFirst().currentAmount().compareTo(BigDecimal.valueOf(100.00))).isEqualTo(0);
        assertThat(savingsGoals.getFirst().targetAmount().compareTo(BigDecimal.valueOf(1000.00))).isEqualTo(0);
        assertThat(savingsGoals.getFirst().startDate()).isEqualTo(LocalDate.parse("2024-01-01"));
        assertThat(savingsGoals.getFirst().endDate()).isEqualTo(LocalDate.parse("2024-06-01"));
    }
}