package io.example.jwt.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.example.jwt.domain.vo.Token;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenProvider implements InitializingBean {

    @Value("${jwt.signature}")
    private String SIGNATURE;
    private Algorithm ALGORITHM;

    @Value("${jwt.issuer}")
    private String ISSUER;

    @Value("${jwt.subject}")
    private final String SUBJECT = "resource-access";

    @Value("${jwt.audience}")
    private final String AUDIENCE = "client-server";

    @Value("${jwt.claim-key}")
    private String CLAIM_KEY;

    @Value("${jwt.access-token-validity-in-seconds-term}")
    private Long ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM;

    @Value("${jwt.refresh-token-validity-in-seconds-term}")
    private Long REFRESH_TOKEN_VALIDITY_IN_SECONDS_TERM;

    //    private static final long AUTH_TIME = 2;
    //    private static final long REFRESH_TIME = 60 * 60 * 24 * 7;

    /**
     * TokenProvider Bean 생성 이후 application.yml의 jwt.signature_key값 로드 완료 후 ALGORITHM 초기화
     */
    @Override
    public void afterPropertiesSet() {
        ALGORITHM = Algorithm.HMAC256(SIGNATURE);
    }

    public Token createToken(String claim) {
        long currentTimeMillis = System.currentTimeMillis();
        Date accessExpired = new Date(System.currentTimeMillis() + (ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM * 1000));

        String accessToken = tokenBuilder(currentTimeMillis, TokenType.ACCESS, claim);
        String refreshToken = tokenBuilder(currentTimeMillis, TokenType.REFRESH, claim);

        return new Token(accessToken, refreshToken, accessExpired);
    }

    private String tokenBuilder(long currentTimeMillis, TokenType tokenType, String claim) {
        long tokenValidityInSecondsTerm = tokenType.equals(TokenType.ACCESS) ?
                ACCESS_TOKEN_VALIDITY_IN_SECONDS_TERM : REFRESH_TOKEN_VALIDITY_IN_SECONDS_TERM;

        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(SUBJECT)
                .withAudience(AUDIENCE)
                .withIssuedAt(new Date(currentTimeMillis))
                .withExpiresAt(new Date(currentTimeMillis + (tokenValidityInSecondsTerm)))
                .withClaim(CLAIM_KEY, claim)
                .sign(Algorithm.HMAC256(SIGNATURE))
                ;
    }
}

enum TokenType {
    ACCESS,
    REFRESH
}