package io.example.jwt.domain.dto.adapter;

import io.example.jwt.domain.entity.Member;
import io.example.jwt.domain.entity.MemberRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : choi-ys
 * @date : 2021/09/03 11:29 오전
 */
@Getter
public class MemberAdapter extends User {

    private Member member;

    public MemberAdapter(Member member) {
        super(member.getEmail(), member.getPassword(),
                member.isEnabled(), member.isEnabled(), member.isEnabled(), member.isEnabled(),
                member.toSimpleGrantedAuthoritySet());
        this.member = member;
    }
}
