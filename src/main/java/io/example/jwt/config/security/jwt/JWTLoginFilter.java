package io.example.jwt.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.jwt.domain.dto.adapter.MemberAdapter;
import io.example.jwt.domain.dto.request.LoginRequest;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private ObjectMapper objectMapper = new ObjectMapper();
    private LoginService loginService;

    public JWTLoginFilter(AuthenticationManager authenticationManager, LoginService loginService) {
        super(authenticationManager);
        this.loginService = loginService;
        setFilterProcessesUrl("/login");
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        UserDetails userDetails = loginService.loadUserByUsername(loginRequest.getEmail());
        return new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getAuthorities()
        );
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException {
        MemberAdapter member = (MemberAdapter) authResult.getPrincipal();
        String accessToken = JWTUtil.makeAuthToken(member.getMember());
        response.setHeader("access_token", accessToken);
        response.setHeader("refresh_token", JWTUtil.makeRefreshToken(member.getMember()));
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//        response.getOutputStream().write(objectMapper.writeValueAsBytes(member));
        Token token = new Token(accessToken, new Date());
        response.getOutputStream().write(objectMapper.writeValueAsBytes(token));
    }
}
