package io.example.jwt.config.security.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import io.example.jwt.domain.entity.Member;
import io.example.jwt.service.LoginService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class JWTCheckFilter extends BasicAuthenticationFilter {

    private LoginService loginService;

    public JWTCheckFilter(AuthenticationManager authenticationManager, LoginService loginService) {
        super(authenticationManager);
        this.loginService = loginService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(bearer == null || !bearer.startsWith("Bearer ")){
            chain.doFilter(request, response);
            return;
        }
        String token = bearer.substring("Bearer ".length());
        VerifyResult result = JWTUtil.verify(token);
        if(result.isSuccess()){
            Member member = (Member) loginService.loadUserByUsername(result.getUsername());
            UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                    member.getEmail(),
                    null,
                    member.getRoles().stream()
                            .map(it -> new SimpleGrantedAuthority(it.name()))
                            .collect(Collectors.toSet())
            );
            SecurityContextHolder.getContext().setAuthentication(userToken);
            chain.doFilter(request, response);
        }else{
            throw new TokenExpiredException("Token is not valid");
        }
    }
}
