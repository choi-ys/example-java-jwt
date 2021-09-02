package io.example.jwt.domain.dto.response;

import io.example.jwt.domain.entity.Member;
import io.example.jwt.domain.entity.MemberRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author : choi-ys
 * @date : 2021/09/02 3:34 오후
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignupResponse {
    private Long id;
    private String email;
    private String name;
    private Set<MemberRole> roles;

    public SignupResponse(Long id, String email, String name, Set<MemberRole> roles) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.roles = roles;
    }

    public static SignupResponse mapTo(Member member) {
        return new SignupResponse(member.getId(), member.getEmail(), member.getName(), member.getRoles());
    }
}
