package io.example.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.example.jwt.domain.vo.Token;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

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

    public Token createToken(){
        long currentTimeMillis = System.currentTimeMillis();
        Date accessExpire = new Date(System.currentTimeMillis() + (accessTokenValidityInSecondsTerm * 1000));
        String token = tokenBuilder(currentTimeMillis, accessExpire);

        return new Token(token, accessExpire);
    }

    private String tokenBuilder(long currentTimeMillis, Date expire){

        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(subject)
                .withAudience(audience)
                .withIssuedAt(new Date(currentTimeMillis))
                .withExpiresAt(expire)
                .sign(Algorithm.HMAC256(SIGNATURE_KEY))
                ;
    }

    public Map<String, Claim> getClaimsMap(String token){
        DecodedJWT verifiedToken = JWT.require(ALGORITHM).build().verify(token);
        return verifiedToken.getClaims();
    }
}
