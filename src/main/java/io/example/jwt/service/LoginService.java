package io.example.jwt.service;

import io.example.jwt.domain.dto.adapter.MemberAdapter;
import io.example.jwt.domain.entity.Member;
import io.example.jwt.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : choi-ys
 * @date : 2021/09/03 9:59 오전
 * @apiNote :
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername ?????????????????????");
        Member member = memberRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("사용자 정보를 찾을 수 없습니다.")
        );
        return new MemberAdapter(member);
    }
}
