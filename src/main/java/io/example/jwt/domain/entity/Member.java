package io.example.jwt.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

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
                    foreignKey = @ForeignKey(name = "TB_MEMBER_ROLE_MEMBER_ID_FOREIGN_KEY")
            )
    )
    @Enumerated(EnumType.STRING)
    private Set<MemberRole> roles = new HashSet<>();

    // * --------------------------------------------------------------
    // * Header : 도메인 생성
    // * @author : choi-ys
    // * @date : 2021-09-02 오전 3:03
    // * --------------------------------------------------------------
    public Member(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.addRoleSet(Set.of(MemberRole.MEMBER));
    }

    // * --------------------------------------------------------------
    // * Header : Entity의 연관관계 설정
    // * Header : 비즈니스 로직
    // * @author : choi-ys
    // * @date : 2021-09-02 오전 3:09
    // * --------------------------------------------------------------
    public void addRoleSet(Set<MemberRole> additionRoleSet){
        roles.addAll(additionRoleSet);
    }

    public void removeRoleSet(Set<MemberRole> removalRoleSet){
        roles.removeAll(removalRoleSet);
    }

    public Set<SimpleGrantedAuthority> toSimpleGrantedAuthoritySet(){
        return roles.stream()
                .map(it -> new SimpleGrantedAuthority(it.name()))
                .collect(Collectors.toSet());
    }
}
