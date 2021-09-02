package io.example.jwt.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(
        name = "member_tb",
        uniqueConstraints = @UniqueConstraint(
                name = "MEMBER_EMAIL_UNIQUE",
                columnNames = "email"
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
    @ElementCollection(fetch = EAGER)
    @CollectionTable(
            name = "member_role_tb",
            joinColumns = @JoinColumn(
                    name = "member_id",
                    foreignKey = @ForeignKey(name = "TB_MEMBER_ROLE_MEMBER_NO_FOREIGN_KEY")
            )
    )
    @Enumerated(EnumType.STRING)
    private Set<MemberRole> roles = Set.of(MemberRole.MEMBER);

    @OneToMany(mappedBy = "member", fetch = EAGER, cascade = CascadeType.ALL)
    private Set<Authority> authorities = Set.of(new Authority(MemberRole.MEMBER.name()));

    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021-09-02 오전 3:03
    // * --------------------------------------------------------------
    public Member(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.authorities.forEach(it -> it.mappingMember(this));
    }

    public void addAuthorities(Set<Authority> add){
        HashSet hashSet = new HashSet(authorities);
        hashSet.addAll(add);
        authorities = hashSet;
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
