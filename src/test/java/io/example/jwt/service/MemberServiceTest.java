package io.example.jwt.service;

import io.example.jwt.domain.dto.request.SignupRequest;
import io.example.jwt.domain.dto.response.SignupResponse;
import io.example.jwt.domain.entity.MemberRole;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : choi-ys
 * @date : 2021/09/02 4:03 오후
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Service:Member")
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class MemberServiceTest {

    private final MemberService memberService;

    @Test
    @DisplayName("signup")
    public void signup() {
        // Given
        String email = "project.log.062@gmail.com";
        String password = "password";
        String name = "choi-ys";
        SignupRequest signupRequest = new SignupRequest(email, name, password);

        // When
        SignupResponse expected = memberService.signup(signupRequest);

        // Then
        assertAll(
                () -> assertNotNull(expected.getId()),
                () -> assertEquals(expected.getEmail(), signupRequest.getEmail()),
                () -> assertEquals(expected.getName(), signupRequest.getName()),
                () -> assertEquals(expected.getRoles(), Set.of(MemberRole.MEMBER))
        );
    }
}
