package io.example.jwt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.jwt.config.EnableMockMvc;
import io.example.jwt.domain.dto.request.LoginRequest;
import io.example.jwt.domain.entity.Member;
import io.example.jwt.domain.vo.Token;
import io.example.jwt.generator.MemberGenerator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author : choi-ys
 * @date : 2021/09/03 12:02 오후
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableMockMvc
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Import(MemberGenerator.class)
@DisplayName("Controller:Login")
@Transactional
class LoginControllerTest {

    private final MemberGenerator memberGenerator;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private final String LOGIN_URL = "/login";
    private final String REFRESH_URL = "/auth/refresh";

    @Test
    @DisplayName("API:login")
    public void login() throws Exception {
        // Given
        Member member = MemberGenerator.member();
        Member savedMember = memberGenerator.savedMember(member.getEmail(), member.getPassword());
        LoginRequest loginRequest = new LoginRequest(savedMember.getEmail(), member.getPassword());

        // When
        ResultActions resultActions = this.mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginRequest))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("accessToken").exists())
                .andExpect(jsonPath("refreshToken").exists())
                .andExpect(jsonPath("accessExpired").exists())
        ;
    }

    @Test
    @DisplayName("API:refresh")
    public void refresh() throws Exception {
        // Given
        Member member = MemberGenerator.member();
        Member savedMember = memberGenerator.savedMember(member.getEmail(), member.getPassword());
        LoginRequest loginRequest = new LoginRequest(savedMember.getEmail(), member.getPassword());

        // When
        ResultActions resultActions = this.mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginRequest))
        );
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        Token token = objectMapper.readValue(contentAsString, Token.class);

        resultActions = this.mockMvc.perform(post(REFRESH_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(token))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("accessToken").exists())
                .andExpect(jsonPath("refreshToken").exists())
                .andExpect(jsonPath("accessExpired").exists())
        ;
    }
}