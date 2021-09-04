package io.example.jwt.config.security;

import io.example.jwt.config.security.endpoint.RolesSecurity;
import io.example.jwt.config.security.jwt.*;
import io.example.jwt.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author : choi-ys
 * @date : 2021/09/02 9:25 오전
 */
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final LoginService loginService;
    private final TokenUtil tokenUtil;
    private final TokenProvider tokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        TokenGenerateFilter loginFilter = new TokenGenerateFilter(authenticationManager(), loginService, tokenUtil, tokenProvider);
        TokenCheckFilter checkFilter = new TokenCheckFilter(authenticationManager(), loginService, tokenUtil);
        TokenRefreshFilter refreshFilter = new TokenRefreshFilter(authenticationManager(), loginService, tokenUtil, tokenProvider);

        http
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(checkFilter, BasicAuthenticationFilter.class)
                .addFilterAt(refreshFilter, BasicAuthenticationFilter.class)
                .authorizeRequests(request ->
                        request
                                .antMatchers(HttpMethod.GET, RolesSecurity.NONE.patterns(HttpMethod.GET)).permitAll()
                                .antMatchers(HttpMethod.POST, RolesSecurity.NONE.patterns(HttpMethod.POST)).permitAll()
                                .anyRequest().authenticated()
                )
        ;
    }
}
