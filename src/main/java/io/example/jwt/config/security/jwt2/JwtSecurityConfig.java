//package io.example.jwt.config.security.jwt2;
//
//import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
///**
// * @author : choi-ys
// * @date : 2021/04/21 2:24 오후
// * @Content : SecurityConfig에 TokenProvider, JwtFilter를 적용하기 위한 설정
// */
//public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
//
//    private TokenProvider tokenProvider;
//
//    public JwtSecurityConfig(TokenProvider tokenProvider) {
//        this.tokenProvider = tokenProvider;
//    }
//
//    @Override
//    public void configure(HttpSecurity http) {
//        JwtFilter customFilter = new JwtFilter(tokenProvider);
//        http.addFilterAt(customFilter, UsernamePasswordAuthenticationFilter.class);
//    }
//}