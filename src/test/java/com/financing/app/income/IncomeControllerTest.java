package com.financing.app.income;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.financing.app.auth.adapter.out.persistence.Token;
import com.financing.app.auth.adapter.out.persistence.TokenRepository;
import com.financing.app.auth.application.domain.service.AuthenticationUseCase;
import com.financing.app.bootstrap_module.exception.ErrorResponse;
import com.financing.app.income.adapter.out.persistence.Income;
import com.financing.app.income.adapter.out.persistence.IncomeRepository;
import com.financing.app.income.application.domain.model.IncomeDTO;
import com.financing.app.income.application.port.in.IncomeRequest;
import com.financing.app.user.adapter.out.User;
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
import java.util.Map;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(classes = {com.financing.app.bootstrap_module.AppApplication.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class IncomeControllerTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IncomeRepository incomeRepository;

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
        var authenticationHelperTest = new AuthenticationHelperTest(authenticationUseCase, tokenRepository);
        token = authenticationHelperTest.registerUserTest();
        var user = userRepository.findByUsername("test123");
        if (user.isPresent()) {
            IntStream.range(1, 12).forEach(idx -> {
                var income = new Income(BigDecimal.valueOf(100), "test", LocalDate.of(2023, idx, 1), "test", user.get());
                incomeRepository.save(income);
            });
            mapper.registerModule(new JavaTimeModule());
        }

    }

    @Test
    void whenRequestingIncomes_withValidUserId_returnsIncomes() throws Exception {
        // When
        var result = mockMvc.perform(get("/api/v1/incomes")
                        .header("Authorization", "Bearer " + token.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        var json = result.getResponse().getContentAsString();
        List<Income> incomes = mapper.readValue(json, new TypeReference<>() {
        });

        var income = incomes.getFirst();
        // Then
        assertThat(incomes).isNotEmpty();
        assertThat(income.getAmount().compareTo(BigDecimal.valueOf(100))).isEqualTo(0);
        assertThat(income.getSource()).isEqualTo("test");
        assertThat(income.getDescription()).isEqualTo("test");
    }

    @Test
    void whenRequestingIncomeHistory_withValidUserIdAndDates_returnsHistory() throws Exception {

        var start = LocalDate.of(2023, 1, 1);
        var end = LocalDate.of(2023, 12, 31);
        // When
        var result = mockMvc.perform(get("/api/v1/incomes/history")
                        .header("Authorization", "Bearer " + token.getToken())
                        .param("startDate", start.toString())
                        .param("endDate", end.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        var json = result.getResponse().getContentAsString();
        Map<Integer, Map<String, List<IncomeDTO>>> history = mapper.readValue(json, new TypeReference<>() {
        });
        List<IncomeDTO> januaryIncomes = history.get(2023).get("January");
        // Then
        assertThat(history).isNotEmpty();
        assertThat(januaryIncomes).isNotEmpty();
        assertThat(januaryIncomes.getFirst().getAmount().compareTo(BigDecimal.valueOf(100.0))).isEqualTo(0);
        assertThat(januaryIncomes.getFirst().getIncomeDate().getYear()).isEqualTo(2023);
        assertThat(januaryIncomes.getFirst().getIncomeDate().getMonth().getValue()).isEqualTo(1);
        assertThat(januaryIncomes.getFirst().getSource()).isEqualTo("test");
        assertThat(januaryIncomes.getFirst().getDescription()).isEqualTo("test");
    }

    @Test
    void whenRequestingIncomeHistory_withValidUserIdAndStartDateAfterEndDate_returnsError() throws Exception {
        var start = LocalDate.of(2023, 12, 31);
        var end = LocalDate.of(2023, 1, 1);
        // When
        var result = mockMvc.perform(get("/api/v1/incomes/history")
                        .header("Authorization", "Bearer " + token.getToken())
                        .param("startDate", start.toString())
                        .param("endDate", end.toString()))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String jsonContent = result.getResponse().getContentAsString();

        var error = mapper.readValue(jsonContent, ErrorResponse.class);
        // Then
        assertThat(error).isInstanceOf(ErrorResponse.class);
        assertThat(error.status()).isEqualTo(400);
        assertThat(error.error()).isNotBlank();
        assertThat(error.message()).isNotBlank();
    }

    @Test
    void whenPostingIncome_withValidIncome_returnsNoContentAndSavesRecord() throws Exception {
        var request = new IncomeRequest(
                BigDecimal.valueOf(100),
                "test",
                LocalDate.now(),
                "test"
        );
        var json = mapper.writeValueAsString(request);
        // When
        mockMvc.perform(post("/api/v1/incomes")
                        .header("Authorization", "Bearer " + token.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent());
        var incomes = incomeRepository.findIncomesByUser(new User(1L));
        // Then
        assertThat(incomes.size()).isEqualTo(45);
        assertThat(incomes.getLast().source()).isEqualTo("test");
        assertThat(incomes.getLast().description()).isEqualTo("test");
    }

    @Test
    void whenPostingIncome_withInvalidIncome_returnsError() throws Exception {
        var request = new IncomeRequest(
                BigDecimal.valueOf(100),
                null,
                LocalDate.now(),
                "test"
        );
        var json = mapper.writeValueAsString(request);
        // When
        var result = mockMvc.perform(post("/api/v1/incomes")
                        .header("Authorization", "Bearer " + token.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is5xxServerError())
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