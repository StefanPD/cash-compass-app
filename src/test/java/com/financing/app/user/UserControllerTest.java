package com.financing.app.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.financing.app.exception.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        var user = new User(1L, "test@email.com", "test", "test123", LocalDateTime.now(), LocalDateTime.now());
        userRepository.save(user);
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void whenRequestUser_givenValidUserId_returnsUser() throws Exception {
        // Given
        var userId = 1L;
        // When
        var result = mockMvc.perform(get("/api/v1/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        var json = result.getResponse().getContentAsString();
        var user = mapper.readValue(json, UserDTO.class);
        // Then
        assertThat(user.getUserId()).isEqualTo(userId);
    }

    @Test
    void whenRequestUser_givenInvalidUserId_returnsError() throws Exception {
        // Given
        var userId = 0L;
        // When
        var result = mockMvc.perform(get("/api/v1/users/{userId}", userId))
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