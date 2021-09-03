package io.example.jwt.repository;

import io.example.jwt.config.JpaTestConfig;
import io.example.jwt.domain.entity.Member;
import io.example.jwt.domain.entity.MemberRole;
import io.example.jwt.generator.MemberGenerator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : choi-ys
 * @date : 2021/09/02 9:53 오전
 */
@DisplayName("Repository:Member")
@RequiredArgsConstructor
@Import(MemberGenerator.class)
class MemberRepositoryTest extends JpaTestConfig {

    private final MemberGenerator memberGenerator;
    private final MemberRepository memberRepository;

    @Test
    @DisplayName("save")
    public void save(){
        // Given
        Member member = MemberGenerator.member();
        clear();

        // When
        Member expected = memberRepository.saveAndFlush(member);

        // Then
        assertAll(
                () -> assertNotNull(expected.getId()),
                () -> assertEquals(expected.getEmail(), member.getEmail()),
                () -> assertEquals(expected.getName(), member.getName()),
                () -> assertEquals(expected.getRoles(), Set.of(MemberRole.MEMBER))
        );
    }

    @Test
    @DisplayName("findById")
    public void findById(){
        // Given
        Member savedMember = memberGenerator.savedMember();
        clear();

        // When
        Member expected = memberRepository.findById(savedMember.getId()).orElseThrow();

        // Then
        assertAll(
                () -> assertEquals(expected.getId(), savedMember.getId()),
                () -> assertEquals(expected.getName(), savedMember.getName()),
                () -> assertEquals(expected.getEmail(), savedMember.getEmail()),
                () -> assertEquals(expected.getRoles(), savedMember.getRoles())
        );
    }
    
    @Test
    @DisplayName("findByEmail")
    public void findByEmail(){
        // Given
        Member savedMember = memberGenerator.savedMember();
        clear();

        // When
        Member expected = memberRepository.findByEmail(savedMember.getEmail()).orElseThrow();

        // Then
        assertAll(
                () -> assertEquals(expected.getId(), savedMember.getId()),
                () -> assertEquals(expected.getName(), savedMember.getName()),
                () -> assertEquals(expected.getEmail(), savedMember.getEmail()),
                () -> assertEquals(expected.getRoles(), savedMember.getRoles())
        );
    }
    
    @Test
    @DisplayName("AddRoles")
    public void AddRoles(){
        // Given
        Member savedMember = memberGenerator.savedMember();
        clear();

        Member persistStatusMember = memberRepository.findById(savedMember.getId()).orElseThrow();
        Set<MemberRole> additionRoleSet = Set.of(MemberRole.ADMIN, MemberRole.SYSTEM_ADMIN);

        // When
        persistStatusMember.addRoleSet(additionRoleSet);
        flush();

        // Then
        assertAll(
                () -> assertTrue(persistStatusMember.getRoles().containsAll(additionRoleSet))
        );
    }

    // TODO 권한 제거 시, 최소 하나 이상의 권한 유지 유효성 처리
    @Test
    @DisplayName("RemoveRoles")
    public void RemoveRoles(){
        // Given
        Member savedMember = memberGenerator.savedMember();
        Set<MemberRole> additionRoleSet = Set.of(MemberRole.ADMIN, MemberRole.SYSTEM_ADMIN);
        savedMember.addRoleSet(additionRoleSet);
        flushAndClearPersistContext();

        Member persistStatusMember = memberRepository.findById(savedMember.getId()).orElseThrow();

        // When
        persistStatusMember.removeRoleSet(Set.of(MemberRole.SYSTEM_ADMIN));
        flush();

        // Then
        assertAll(
                () -> assertTrue(!persistStatusMember.getRoles().contains(MemberRole.SYSTEM_ADMIN))
        );
    }
}