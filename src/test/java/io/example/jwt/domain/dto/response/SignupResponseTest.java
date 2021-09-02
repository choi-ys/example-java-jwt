package io.example.jwt.domain.dto.response;

import io.example.jwt.domain.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author : choi-ys
 * @date : 2021/09/02 3:54 오후
 */
@DisplayName("DTO:SignUpResponse")
class SignupResponseTest {

    @Test
    @DisplayName("mapTo")
    public void mapTo() {
        // Given
        String email = "project.log.062@gmail.com";
        String password = "password";
        String name = "choi-ys";
        Member member = new Member(email, name, password);

        // When
        SignupResponse signupResponse = SignupResponse.mapTo(member);

        // Then
        System.out.println(signupResponse);
    }
}
