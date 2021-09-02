package io.example.jwt.domain.dto.request;

import io.example.jwt.domain.entity.Member;
import io.example.jwt.domain.entity.MemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : choi-ys
 * @date : 2021/09/02 3:42 오후
 */
@DisplayName("DTO:SignupRequest")
class SignupRequestTest {

    @Test
    @DisplayName("toEntity")
    public void toEntity() {
        // Given
        String email = "project.log.062@gmail.com";
        String name = "choi-ys";
        String password = "password";

        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        SignupRequest signupRequest = new SignupRequest(email, name, password);

        // When
        Member member = signupRequest.toEntity(passwordEncoder);

        // Then
        assertAll(
                () -> assertEquals(member.getEmail(), signupRequest.getEmail()),
                () -> assertEquals(member.getName(), signupRequest.getName()),
                () -> assertTrue(passwordEncoder.matches(signupRequest.getPassword(), member.getPassword())),
                () -> assertEquals(member.getRoles(), Set.of(MemberRole.MEMBER))
        );
    }
}
