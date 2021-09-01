package io.example.jwt.utils;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Map;

/**
 * @author : choi-ys
 * @date : 2021/09/01 6:40 오후
 * @apiNote :
 */
public class TokenUtils {

    public static Map getHeaderMap(DecodedJWT decodedJWT) throws JsonProcessingException {
        String headerJson = new String(Base64.getDecoder().decode(decodedJWT.getHeader()));
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(headerJson, Map.class);
    }

    public static Claim getClaimValue(Map claimsMap, String key){
        return (Claim) claimsMap.get(key);
    }

}
