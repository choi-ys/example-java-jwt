package io.example.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.example.jwt.config.security.jwt2.TokenProvider;
import io.example.jwt.domain.vo.Token;
import io.example.jwt.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : choi-ys
 * @date : 2021/09/01 5:42 오후
 */
@SpringBootTest(classes = TokenProvider.class)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DisplayName("Component:TokenProvider")
class TokenProviderTest {

    @Value("${jwt.signature_key}")
    String signature_key;

    @Value("${jwt.issuer}")
    String issuer;

    @Value("${jwt.access-token-validity-in-seconds-term}")
    Long accessTokenValidityInSecondsTerm;

    private final TokenProvider tokenProvider;

    @Test
    @DisplayName("load jwt configuration by application.yml")
    public void loadJwtConfigurationByApplicationYml() {
        System.out.println(signature_key);
        System.out.println(issuer);
        System.out.println(accessTokenValidityInSecondsTerm);
    }

    @Test
    @DisplayName("createToken")
    public void createToken() throws JsonProcessingException {
        // When
        Token token = tokenProvider.createToken();

        // Then
        DecodedJWT verifiedToken = JWT.require(Algorithm.HMAC256(signature_key)).build().verify(token.getAccessToken());

        Map headerMap = TokenUtils.getHeaderMap(verifiedToken);
        assertAll(
                () -> assertEquals(headerMap.get("alg"), Algorithm.HMAC256(signature_key).getName()),
                () -> assertEquals(headerMap.get("typ"), "JWT"),
                () -> assertEquals(token.getAccessExpire().getTime() / 1000, verifiedToken.getClaim("exp").asLong()
                        , "millisecond 부분이 제거된 token 객체의 accessExpire 값과 payload의 exp 항목값의 동일 여부를 확인한다.")
        );
    }

    @Test
    @DisplayName("getClaimsMap")
    public void getClaimsMap() {
        // Given
        Token token = tokenProvider.createToken();
        Set<String> expectedClaimsKeySet = Set.of("iss", "sub", "aud", "iat", "exp");

        // When
        Map<String, Claim> claimsMap = tokenProvider.getClaimsMap(token.getAccessToken());

        // Then
        assertTrue(claimsMap.entrySet().stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet())
                .containsAll(expectedClaimsKeySet), "발급된 jwt의 payload 항목이 권장 spec 정보 포함 여부를 확인한다.");
    }
}