package com.financing.app.savings.goals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.financing.app.exception.ErrorResponse;
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
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mapper.registerModule(new JavaTimeModule());
        var user = new User(1L, "test@email.com", "test", "test123", LocalDateTime.now(), LocalDateTime.now());
        var savingGoal = new SavingsGoal(1L, "vacation", BigDecimal.valueOf(1000.00), BigDecimal.valueOf(100.00), LocalDate.parse("2024-01-01"), LocalDate.parse("2024-06-01"), user);

        userRepository.save(user);
        savingsGoalRepository.save(savingGoal);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void whenRequestingSavingGoals_withValidUserId_returnsSavingGoals() throws Exception {
        // Given
        Long userId = 1L;

        // When
        var result = mockMvc.perform(get("/users/{userId}/savings-goals", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String jsonContent = result.getResponse().getContentAsString();

        List<SavingsGoalDTO> savingsGoals = mapper.readValue(jsonContent, new TypeReference<>() {
        });

        // Then
        assertThat(savingsGoals).isNotEmpty();
        assertThat(savingsGoals.getFirst().getName()).isEqualTo("vacation");
        assertThat(savingsGoals.getFirst().getCurrentAmount().compareTo(BigDecimal.valueOf(100.00))).isEqualTo(0);
        assertThat(savingsGoals.getFirst().getTargetAmount().compareTo(BigDecimal.valueOf(1000.00))).isEqualTo(0);
        assertThat(savingsGoals.getFirst().getStartDate()).isEqualTo(LocalDate.parse("2024-01-01"));
        assertThat(savingsGoals.getFirst().getEndDate()).isEqualTo(LocalDate.parse("2024-06-01"));
    }

    @Test
    void whenRequestingSavingsGoals_withInvalidUserId_returnsError() throws Exception {
        // Given
        Long userId = 0L;

        // When
        var result = mockMvc.perform(get("/users/{userId}/savings-goals", userId))
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
}