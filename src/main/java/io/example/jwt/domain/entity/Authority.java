package io.example.jwt.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

/**
 * @author : choi-ys
 * @date : 2021/09/02 12:31 오후
 * @apiNote :
 */
@Entity
@Table(name = "member_authority_tb")
@Getter
@NoArgsConstructor(access = PROTECTED)
@IdClass(Authority.class)
public class Authority implements GrantedAuthority {

    @Id
    private String authority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "TB_MEMBER_AUTHORITY_MEMBER_NO_FOREIGN_KEY"))
    private Member member;

    public Authority(String authority) {
        this.authority = authority;
    }

    public void mappingMember(Member member) {
        this.member = member;
    }
}
