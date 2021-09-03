package io.example.jwt.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.jwt.domain.dto.adapter.MemberAdapter;
import io.example.jwt.domain.vo.Token;
import io.example.jwt.service.LoginService;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTRefreshFilter extends UsernamePasswordAuthenticationFilter {

    private ObjectMapper objectMapper = new ObjectMapper();
    private LoginService loginService;

    public JWTRefreshFilter(AuthenticationManager authenticationManager, LoginService loginService) {
        super(authenticationManager);
        this.loginService = loginService;
        setFilterProcessesUrl("/auth/refresh");
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        Token token = objectMapper.readValue(request.getInputStream(), Token.class);
        VerifyResult verify = JWTUtil.verify(token.getAccessToken());
        if (verify.isSuccess()) {
            UserDetails userDetails = loginService.loadUserByUsername(verify.getUsername());
            return new UsernamePasswordAuthenticationToken(
                    userDetails, userDetails.getAuthorities()
            );
        }
        return null;
    }

    /**
     * TODO refresh 요청 처리 보완
     * - refresh 요청 시 기 발급 access-token, refresh-token blacklist(만료) 처리
     */
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException {
        MemberAdapter member = (MemberAdapter) authResult.getPrincipal();
        response.setHeader("auth_token", JWTUtil.makeAuthToken(member.getMember()));
        response.setHeader("refresh_token", JWTUtil.makeRefreshToken(member.getMember()));
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(member));
    }
}
