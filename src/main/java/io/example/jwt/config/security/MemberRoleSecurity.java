package io.example.jwt.config.security;

import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021-09-02 오전 7:02
 */
public enum MemberRoleSecurity {

    NONE(Arrays.asList(
            new AuthRequest(HttpMethod.GET, Arrays.asList(
                    "/index")),
            new AuthRequest(HttpMethod.POST, Arrays.asList(
                    "/member/signup"
            ))
    ));

    private List<AuthRequest> matchers;

    MemberRoleSecurity(List<AuthRequest> matchers) {
        this.matchers = matchers;
    }

    public String[] patterns(HttpMethod httpMethod) {
        return matchers.stream()
                .filter(it -> it.getHttpMethod().equals(httpMethod))
                .map(it -> it.getPatterns())
                .flatMap(arr -> Arrays.stream(arr.toArray(String[]::new)))
                .toArray(String[]::new)
                ;
    }
}
