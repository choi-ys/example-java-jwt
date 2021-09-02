package io.example.jwt.domain.dto.request;

import io.example.jwt.domain.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author : choi-ys
 * @date : 2021/09/02 3:34 오후
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignupRequest {
    private String email;
    private String name;
    private String password;

    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021/09/02 3:47 오후
    // * --------------------------------------------------------------
    public SignupRequest(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    // * --------------------------------------------------------------
    // * Header : 객체 변환
    // * Content : 회원 요청 객체를 Member Entity로 변환 시, 비밀번호 항목 암호화 처리
    // * @author : choi-ys
    // * @date : 2021/09/02 3:47 오후
    // * --------------------------------------------------------------
    public Member toEntity(PasswordEncoder passwordEncoder) {
        return new Member(email, name, passwordEncoder.encode(password));
    }
}
