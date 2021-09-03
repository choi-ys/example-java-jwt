package io.example.jwt.config.security.endpoint;

import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;

/**
 * @author : choi-ys
 * @date : 2021-09-02 오전 7:02
 */
public enum RolesSecurity {

    NONE(Arrays.asList(
            new RequestAuthentication(HttpMethod.GET, Arrays.asList(
                    "/index")),
            new RequestAuthentication(HttpMethod.POST, Arrays.asList(
                    "/member/signup"
            ))
    ));

    private List<RequestAuthentication> matchers;

    RolesSecurity(List<RequestAuthentication> matchers) {
        this.matchers = matchers;
    }

    public String[] patterns(HttpMethod httpMethod) {
        return matchers.stream()
                .filter(it -> it.getHttpMethod().equals(httpMethod))
                .map(it -> it.getScope())
                .flatMap(arr -> Arrays.stream(arr.toArray(String[]::new)))
                .toArray(String[]::new)
                ;
    }
}
