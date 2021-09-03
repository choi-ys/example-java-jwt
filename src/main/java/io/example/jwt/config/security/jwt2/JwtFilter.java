//package io.example.jwt.config.security.jwt2;
//
//import io.example.jwt.config.security.jwt2.TokenProvider;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.GenericFilterBean;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//import static org.springframework.http.HttpHeaders.AUTHORIZATION;
//
///**
// * @author : choi-ys
// * @date : 2021/04/21 2:20 오후
// * @Content : SecurityContext애 발급 토큰의 인증정보 저장
// */
//@RequiredArgsConstructor
//@Slf4j
//@Configuration
//public class JwtFilter extends GenericFilterBean {
//
//    private final TokenProvider tokenProvider;
//
//    /**
//     * 요청 시 해당 Filter를 통해 요청의 토큰정보로 부터 Authentication 객체를 Spring Context에 저장
//     *
//     * @param servletRequest
//     * @param servletResponse
//     * @param filterChain
//     * @throws IOException
//     * @throws ServletException
//     */
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
//        String jwt = resolveToken(httpServletRequest);
//        String requestURI = httpServletRequest.getRequestURI();
//        String httpMethod = httpServletRequest.getMethod();
//
//        /**
//         * 요청 객체의 header에 포함된 토큰 정보의 유효성 검사
//         * 토큰의 Authentication 정보 추출
//         * authentication정보를 SpringContext에 저장
//         */
//        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
////            Authentication authentication = tokenProvider.getAuthentication(jwt);
////            SecurityContextHolder.getContext().setAuthentication(authentication);
////            log.info("[RequestUser: {}][URI: {}[{}], Authority: {}]", authentication.getName(), requestURI, httpMethod, authentication.getAuthorities());
//            log.info("??");
//        } else {
//            log.info("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
//        }
//
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//
//    /**
//     * 요청객체(HttpServletRequest)의 Header에서 토큰 정보 추출
//     *
//     * @param request
//     * @return
//     */
//    private String resolveToken(HttpServletRequest request) {
//        String bearerToken = request.getHeader(AUTHORIZATION);
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//}