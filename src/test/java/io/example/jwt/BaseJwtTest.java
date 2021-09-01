package io.example.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.Date;
import java.util.Map;

import static io.example.jwt.utils.TokenUtils.getClaimValue;
import static io.example.jwt.utils.TokenUtils.getHeaderMap;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author : choi-ys
 * @date : 2021/09/01 12:36 오후
 */
@DisplayName("Base:java-jwt")
public class BaseJwtTest {

    /**
     * Header
     *  - alg : 토큰 해싱 알고리즘
     *  - typ : 토큰 타입
     */
    private final String SIGNATURE_KEY = "test-signature-key";
    private final Algorithm ALGORITHM = Algorithm.HMAC256(SIGNATURE_KEY);

    /**
     * Payload(Claims)
     *  - iss : Issuer, 토큰 발행 주체
     *  - sub : Subject, 무엇에 관한 토큰인지
     *  - aud : Audience, 토큰 발행 대상
     *  - exp : Expiration, 토큰 만료 일시
     *  - iat : Issued At, 토큰 발행 일시
     *  - nbf : Not Before, 토큰의 유효성 시작 일시
     *  - jti : JWT ID, 토큰 아이디
     *  - 그 외, 인증에 필요하거나 대상서버에서 필요로 하는 데이터
     */
    private final String issuer = "authorization-server";
    private final String subject = "resource-access";
    private final String audience = "client-server";
    private final Long accessValidityInSeconds = 1L;

    private String generateJwt(){
        String key = "name";
        String value = "choi";

        long now = System.currentTimeMillis();

        return JWT.create()
                .withIssuer(issuer)
                .withSubject(subject)
                .withAudience(audience)
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now + (accessValidityInSeconds * 1000)))
                .withClaim(key, value)
                .sign(Algorithm.HMAC256(SIGNATURE_KEY))
                ;
    }

    @Test
    @DisplayName("java-jwt 토큰 생성 및 검증")
    public void createdJwtAndVerify() throws JsonProcessingException {
        // Given
        String jwt = generateJwt();

        // When
        DecodedJWT expected = JWT.require(Algorithm.HMAC256(SIGNATURE_KEY)).build().verify(jwt);

        // Then
        assertJwt(expected);
    }

    @Test
    @DisplayName("잘못된 서명을 가진 토큰")
    public void unsupportedJwtException(){
        // Given
        String jwt = generateJwt();
        String incorrectKey = "incorrect-key";

        // When & Then
        assertThrows(SignatureVerificationException.class, () -> JWT.require(Algorithm.HMAC256(incorrectKey)).build().verify(jwt));
    }

    @Test
    @DisplayName("유효하지 형식의 못한 토큰")
    public void jWTDecodeException(){
        // Given
        String inValidJwtFormat = "header.payload";
        String inValidJsonFormat = "header.payload.signature";

        String base64EncodedHeader = new String(Base64.getEncoder().encode("header".getBytes()));
        String base64EncodedPayload = new String(Base64.getEncoder().encode("payload".getBytes()));
        String base64EncodedSignature = new String(Base64.getEncoder().encode("signature".getBytes()));
        String inValidBase64Encoded = base64EncodedHeader
                .concat(".").concat(base64EncodedPayload)
                .concat(".").concat(base64EncodedSignature);

        System.out.println(inValidBase64Encoded);

        // When & Then
        assertThrows(JWTDecodeException.class, () -> JWT.require(ALGORITHM).build().verify(inValidJwtFormat));
        assertThrows(JWTDecodeException.class, () -> JWT.require(ALGORITHM).build().verify(inValidJsonFormat));
        assertThrows(JWTDecodeException.class, () -> JWT.require(ALGORITHM).build().verify(inValidBase64Encoded));
    }

    @Test
    @DisplayName("유효기간이 만료된 토큰")
    public void expiredJwt() throws InterruptedException {
        // Given
        String jwt = generateJwt();

        // When & Then
        Thread.sleep(2000L);
        assertThrows(TokenExpiredException.class, () -> JWT.require(ALGORITHM).build().verify(jwt));
    }

    private void assertJwt(DecodedJWT expected) throws JsonProcessingException {
        Map headerMap = getHeaderMap(expected);
        Map claimsMap = expected.getClaims();

        assertAll(
                () -> assertJwtHeader(headerMap),
                () -> assertJwtClaims(claimsMap)
        );
    }

    private void assertJwtHeader(Map headerMap){
        assertAll(
                () -> assertEquals(headerMap.get("alg"), ALGORITHM.getName()),
                () -> assertEquals(headerMap.get("typ"), "JWT")
        );
    }

    private void assertJwtClaims(Map claimsMap){
        assertAll(
                () -> assertEquals(getClaimValue(claimsMap, "aud").asString(), audience),
                () -> assertEquals(getClaimValue(claimsMap, "sub").asString(), subject),
                () -> assertEquals(getClaimValue(claimsMap, "iss").asString(), issuer),
                () -> assertNotNull(getClaimValue(claimsMap, "iat")),
                () -> assertEquals(getClaimValue(claimsMap, "exp").asLong(), getClaimValue(claimsMap, "iat").asLong() + accessValidityInSeconds)
        );
    }
}