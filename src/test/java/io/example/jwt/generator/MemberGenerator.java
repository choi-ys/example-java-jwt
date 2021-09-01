package io.example.jwt.generator;

import io.example.jwt.domain.entity.Member;

/**
 * @author : choi-ys
 * @date : 2021-09-02 오전 4:24
 */
public class MemberGenerator {
    private static final String email = "project.log.062@gmail.com";
    private static final String name = "choi-ys";
    private static final String password = "password";

    public static Member member(){
        return member(email, name);
    }

    public static Member member(String email, String name){
        return new Member(email, name, password);
    }
}
