package io.example.jwt.config.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * @author : choi-ys
 * @date : 2021/09/02 9:25 오전
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(request ->
                        request
                                .antMatchers(HttpMethod.GET, MemberRoleSecurity.NONE.patterns(HttpMethod.GET)).permitAll()
                                .antMatchers(HttpMethod.POST, MemberRoleSecurity.NONE.patterns(HttpMethod.POST)).permitAll()
                                .anyRequest().authenticated()
                )
        ;
    }
}
