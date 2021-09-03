package io.example.jwt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.jwt.config.EnableMockMvc;
import io.example.jwt.domain.dto.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author : choi-ys
 * @date : 2021/09/02 4:20 오후
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableMockMvc
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DisplayName("Controller:Member")
class MemberControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    @DisplayName("API:signup")
    public void newMock() throws Exception {
        // Given
        String email = "project.log.062@gmail.com";
        String name = "최용석";
        String password = "password";
        SignupRequest signupRequest = new SignupRequest(email, name, password);

        // When
        String urlTemplate = "/member";
        ResultActions resultActions = this.mockMvc.perform(post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest))
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
        ;
    }
}
