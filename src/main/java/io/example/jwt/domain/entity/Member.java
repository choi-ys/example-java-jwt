package io.example.jwt.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(
        name = "member_tb",
        uniqueConstraints = @UniqueConstraint(
                name = "MEMBER_EMAIL_UNIQUE",
                columnNames = "member_email"
        ))
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    // * --------------------------------------------------------------
    // * Header : Member <-> MemberRoles Entity의 연관관계 설정
    // * @author : choi-ys
    // * @date : 2021-09-02 오전 3:09
    // * --------------------------------------------------------------
    @ElementCollection(fetch = LAZY)
    @CollectionTable(
            name = "member_role_tb",
            joinColumns = @JoinColumn(
                    name = "partner_no",
                    foreignKey = @ForeignKey(name = "TB_PARTNER_ROLE_PARTNER_NO_FOREIGN_KEY")
            )
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", nullable = false, length = 20)
    private Set<MemberRole> roles = Set.of(MemberRole.MEMBER);

    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021-09-02 오전 3:03
    // * --------------------------------------------------------------
    public Member(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    // * --------------------------------------------------------------
    // * Header : Entity의 연관관계 설정
    // * Header : 비즈니스 로직
    // * @author : choi-ys
    // * @date : 2021-09-02 오전 3:09
    // * --------------------------------------------------------------
    public void addRoleSet(Set<MemberRole> additionRoleSet){
        HashSet memberRoles = new HashSet(roles);
        memberRoles.addAll(additionRoleSet);
        roles = memberRoles;
    }

    public void removeRoleSet(Set<MemberRole> removalRoleSet){
        HashSet memberRoles = new HashSet(roles);
        memberRoles.removeAll(removalRoleSet);
        roles = memberRoles;
    }
}
