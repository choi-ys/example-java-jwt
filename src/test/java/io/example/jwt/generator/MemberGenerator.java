package io.example.jwt.generator;

import io.example.jwt.domain.entity.Member;
import io.example.jwt.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.TestConstructor;

/**
 * @author : choi-ys
 * @date : 2021-09-02 오전 4:24
 */
@TestComponent
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class MemberGenerator {
    private final MemberRepository memberRepository;

    private static final String email = "project.log.062@gmail.com";
    private static final String name = "choi-ys";
    private static final String password = "password";

    public static Member member() {
        return member(email, name);
    }

    public static Member member(String email, String name) {
        return new Member(email, name, password);
    }

    public Member savedMember() {
        return memberRepository.saveAndFlush(member());
    }

    public Member savedMember(String email, String name){
        return memberRepository.saveAndFlush(member(email, name));
    }
}
