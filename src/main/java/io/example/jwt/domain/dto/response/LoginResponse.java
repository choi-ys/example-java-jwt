package io.example.jwt.domain.dto.response;

import io.example.jwt.domain.entity.MemberRole;
import io.example.jwt.domain.vo.Token;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author : choi-ys
 * @date : 2021/09/03 10:00 오전
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse {

    private Long id;
    private String email;
    private String name;
    private Set<MemberRole> roles;
    private Token token;

}
