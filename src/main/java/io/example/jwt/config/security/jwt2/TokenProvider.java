package io.example.jwt.config.security.jwt2;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.example.jwt.domain.vo.Token;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : choi-ys
 * @date : 2021/09/01 4:37 오후
 * @apiNote :
 */
@Component
public class TokenProvider implements InitializingBean {

    @Value("${jwt.signature_key}")
    private String SIGNATURE_KEY;
    private Algorithm ALGORITHM;

    @Value("${jwt.issuer}")
    private String ISSUER;

    private final String subject = "resource-access";
    private final String audience = "client-server";

    @Value("${jwt.access-token-validity-in-seconds-term}")
    Long accessTokenValidityInSecondsTerm;

    /**
     * TokenProvider Bean 생성 이후 application.yml의 jwt.signature_key값 로드 완료 후 ALGORITHM 초기화
     */
    @Override
    public void afterPropertiesSet() {
        ALGORITHM = Algorithm.HMAC256(SIGNATURE_KEY);
    }

    public Token createToken() {
        long currentTimeMillis = System.currentTimeMillis();
        Date accessExpire = new Date(System.currentTimeMillis() + (accessTokenValidityInSecondsTerm * 1000));
        String token = tokenBuilder(currentTimeMillis, accessExpire);

        return new Token(token, null, accessExpire);
    }

    private String tokenBuilder(long currentTimeMillis, Date expire) {

        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(subject)
                .withAudience(audience)
                .withIssuedAt(new Date(currentTimeMillis))
                .withExpiresAt(expire)
                .sign(Algorithm.HMAC256(SIGNATURE_KEY))
                ;
    }

    public Map<String, Claim> getClaimsMap(String token) {
        DecodedJWT verifiedToken = JWT.require(ALGORITHM).build().verify(token);
        return verifiedToken.getClaims();
    }

//    /**
//     * 토큰에 포함된 정보를 이용한 Authentication 객체 반환
//     *
//     * @param token 발급된 토큰 정보
//     * @return 권한 정보 객체
//     */
//    public Authentication getAuthentication(String token) {
//        Map<String, Claim> claimsMap = getClaimsMap(token);
//
//        /**
//         * Claim 객체로부터 권한 정보 추출
//         */
//        Collection<? extends GrantedAuthority> authorities =
//                Arrays.stream(claimsMap.get(AUTHORITIES_KEY).toString().split(","))
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());
//
//        /**
//         * authorities의 권한정보를 이용한 Spring Security User객체 생성
//         */
//        User principal = new User(claims.getAudience(), "", authorities);
//
//        /**
//         * principal, token, authorities를 이용한 Authentication 객체 반환
//         */
//        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
//    }

    /**
     * 토큰 유효성 검증
     *
     * @param token 발급된 토큰
     * @return 토큰의 유효성 여부
     */
    public boolean validateToken(String token) {
        try {
            DecodedJWT verifiedToken = JWT.require(ALGORITHM).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}
