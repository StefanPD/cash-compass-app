package com.financing.app.budget;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.client.MockRestServiceServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BudgetControllerAcceptanceTest {

    @Autowired
    private TestRestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate.getRestTemplate());
        mapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void tearDown() {
        mockServer.reset();
    }

    @Test
    void whenRequestingBudgets_withValidUserId_returnsBudgetDetails() throws JsonProcessingException {
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