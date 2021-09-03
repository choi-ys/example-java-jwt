package io.example.jwt.service;

import io.example.jwt.domain.entity.Member;
import io.example.jwt.generator.MemberGenerator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestConstructor;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author : choi-ys
 * @date : 2021/09/03 11:44 오전
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DisplayName("Service:Login")
@Import(MemberGenerator.class)
class LoginServiceTest {

    private final MemberGenerator memberGenerator;
    private final LoginService loginService;

    @Test
    @DisplayName("loadUserByUsername")
    void loadUserByUsername() {
        // Given
        Member savedMember = memberGenerator.savedMember();

        // When
        UserDetails expected = loginService.loadUserByUsername(savedMember.getEmail());

        // Then
        assertAll(
                () -> assertEquals(expected.getUsername(), savedMember.getEmail()),
                () -> assertEquals(expected.getPassword(), savedMember.getPassword()),
                () -> assertEquals(expected.getAuthorities(), savedMember.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toSet()))
        );
    }
}