package io.example.jwt.config.security.endpoint;

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
public class RequestAuthentication {
    private HttpMethod httpMethod;
    private List<String> scope;

    public RequestAuthentication(HttpMethod httpMethod, List<String> scope) {
        this.httpMethod = httpMethod;
        this.scope = scope;
    }
}
