package io.example.jwt.domain.entity;

import io.example.jwt.generator.MemberGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : choi-ys
 * @date : 2021-09-02 오전 3:56
 */
@DisplayName("Entity:Member")
class MemberTest {

    @Test
    @DisplayName("NoArgsConstructor")
    public void noArgsConstructor(){
        // When
        Member member = new Member();

        // Then
        assertNotNull(member);
    }
    
    @Test
    @DisplayName("Create")
    public void createMember(){
        // Given
        String email = "project.log.062@gmail.com";
        String name = "choi-ys";
        String password = "password";

        // When
        Member member = new Member(email, name, password);
    
        // Then
        assertAll(
                () -> assertEquals(member.getEmail(), email),
                () -> assertEquals(member.getName(), name),
                () -> assertEquals(member.getPassword(), password),
                () -> assertEquals(member.getRoles(), Set.of(MemberRole.MEMBER))
        );
    }

    @Test
    @DisplayName("Add Roles")
    public void addMemberRoleSet(){
        // Given
        Member member = MemberGenerator.member();
        Set<MemberRole> beforeRoleSet = member.getRoles();
        Set<MemberRole> additionRoleSet = Set.of(MemberRole.ADMIN, MemberRole.SYSTEM_ADMIN);

        // When
        member.addRoleSet(additionRoleSet);

        // Then
        assertAll(
                () -> assertTrue(member.getRoles().containsAll(beforeRoleSet)),
                () -> assertTrue(member.getRoles().containsAll(additionRoleSet))
        );
    }

    @Test
    @DisplayName("Remove Roles")
    public void removeMemberRoles(){
        // Given
        Member member = MemberGenerator.member();
        HashSet<MemberRole> beforeRoleSet = new HashSet<>(member.getRoles());

        Set<MemberRole> removalRoleSet = Set.of(MemberRole.ADMIN, MemberRole.SYSTEM_ADMIN);
        beforeRoleSet.removeAll(removalRoleSet);

        // When
        member.removeRoleSet(removalRoleSet);

        // Then
        assertAll(
                () -> assertTrue(!member.getRoles().containsAll(removalRoleSet)),
                () -> assertEquals(member.getRoles(), beforeRoleSet)
        );
    }
}