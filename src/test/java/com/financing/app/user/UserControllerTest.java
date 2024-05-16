package com.financing.app.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.financing.app.auth.adapter.out.persistence.Token;
import com.financing.app.auth.adapter.out.persistence.TokenRepository;
import com.financing.app.auth.application.domain.service.AuthenticationUseCase;
import com.financing.app.bootstrap_module.exception.ErrorResponse;
import com.financing.app.user.adapter.out.UserRepository;
import com.financing.app.user.application.domain.model.UserDTO;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(classes = {com.financing.app.bootstrap_module.AppApplication.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

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
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void whenRequestUser_givenValidUserId_returnsUser() throws Exception {
        // Given
        var userId = 1L;

        // When
        var result = mockMvc.perform(get("/api/v1/users/{userId}", userId)
                        .header("Authorization", "Bearer " + token.getToken()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        var json = result.getResponse().getContentAsString();
        var user = mapper.readValue(json, UserDTO.class);
        // Then
        assertThat(user.getEmail()).isEqualTo("test@email.com");
    }

    @Test
    void whenRequestUser_givenInvalidUserId_returnsError() throws Exception {
        // Given
        var userId = 0L;
        // When
        var result = mockMvc.perform(get("/api/v1/users/{userId}", userId)
                        .header("Authorization", "Bearer " + token.getToken()))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        var json = result.getResponse().getContentAsString();
        var error = mapper.readValue(json, ErrorResponse.class);
        // Then
        assertThat(error.message()).isNotBlank();
        assertThat(error.error()).isNotBlank();
        assertThat(error.status()).isEqualTo(500);
    }
}