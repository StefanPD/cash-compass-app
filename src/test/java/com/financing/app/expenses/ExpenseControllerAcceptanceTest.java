package com.financing.app.expenses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.client.MockRestServiceServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExpenseControllerAcceptanceTest {

    @Autowired
    private TestRestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void whenRequestingExpense_withValidUserId_returnsExpenses() throws JsonProcessingException {
    }

    @Test
    void whenRequestingExpense_withInvalidUserId_returnsExpenses() throws JsonProcessingException {
    }

    @Test
    void whenUploadingExpense_withValidExpense_returnsSuccess() throws JsonProcessingException {
    }
}