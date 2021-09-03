package io.example.jwt.config.security;

import io.example.jwt.config.security.endpoint.RolesSecurity;
import io.example.jwt.config.security.jwt.JWTCheckFilter;
import io.example.jwt.config.security.jwt.JWTLoginFilter;
import io.example.jwt.config.security.jwt.JWTRefreshFilter;
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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JWTLoginFilter loginFilter = new JWTLoginFilter(authenticationManager(), loginService);
        JWTCheckFilter checkFilter = new JWTCheckFilter(authenticationManager(), loginService);
        JWTRefreshFilter refreshFilter = new JWTRefreshFilter(authenticationManager(), loginService);

        http
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //                .addFilterAt(JwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(checkFilter, BasicAuthenticationFilter.class)
                .addFilterAt(refreshFilter, BasicAuthenticationFilter.class)
                .authorizeRequests(request ->
                        request
                                .antMatchers(HttpMethod.GET, RolesSecurity.NONE.patterns(HttpMethod.GET)).permitAll()
                                .antMatchers(HttpMethod.POST, RolesSecurity.NONE.patterns(HttpMethod.POST)).permitAll()
                                .anyRequest().authenticated()
                )
        //                .apply(new JwtSecurityConfig(tokenProvider))
        ;
    }
}
