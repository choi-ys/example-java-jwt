package io.example.jwt.repository;

import io.example.jwt.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : choi-ys
 * @date : 2021/09/02 9:52 오전
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);
}
