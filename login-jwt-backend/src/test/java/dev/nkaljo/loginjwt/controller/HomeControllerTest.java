package dev.nkaljo.loginjwt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.nkaljo.loginjwt.configuration.SecurityConfig;
import dev.nkaljo.loginjwt.model.LoginRequest;
import dev.nkaljo.loginjwt.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest({HomeController.class, AuthController.class})
@Import({SecurityConfig.class, TokenService.class})
public class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void rootWhenUnauthenticatedThen401() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rootWhenAuthenticatedThenSaysHelloUser() throws Exception {
        final var request = new LoginRequest("admin", "admin");
        final var result = mockMvc.perform(post("/login")
                        .content(new ObjectMapper().writeValueAsString(request))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        final var token = result.getResponse().getContentAsString();

        mockMvc.perform(get("/")
                .header("Authorization", "Bearer " + token))
                .andExpect(content().string("Hello admin"));
    }

    @Test
    @WithMockUser
    public void rootWithMockUserStatusIsOk() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }

}
