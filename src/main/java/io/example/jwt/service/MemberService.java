package io.example.jwt.service;

import io.example.jwt.domain.dto.request.SignupRequest;
import io.example.jwt.domain.dto.response.SignupResponse;
import io.example.jwt.domain.entity.Member;
import io.example.jwt.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : choi-ys
 * @date : 2021/09/02 9:52 오전
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupResponse signup(SignupRequest signupRequest) {
        if (memberRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일 입니다.");
        }
        Member savedMember = memberRepository.save(signupRequest.toEntity(passwordEncoder));
        return SignupResponse.mapTo(savedMember);
    }
}
