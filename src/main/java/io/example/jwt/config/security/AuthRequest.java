package io.example.jwt.config.security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021-09-02 오전 7:06
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthRequest {
    private HttpMethod httpMethod;
    private List<String> patterns;

    public AuthRequest(HttpMethod httpMethod, List<String> patterns) {
        this.httpMethod = httpMethod;
        this.patterns = patterns;
    }
}
