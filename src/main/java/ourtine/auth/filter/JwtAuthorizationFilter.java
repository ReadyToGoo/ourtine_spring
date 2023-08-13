package ourtine.auth.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ourtine.auth.service.UserDetailsServiceImpl;
import ourtine.auth.utils.JwtUtil;
import ourtine.domain.UserDetailsImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        log.info("JWT 토큰 추출 및 Authentication 객체 생성 및 반환");

        String url = req.getRequestURI();

        log.info("url = " + url);

        if(StringUtils.hasText(url) && url.startsWith("/api/auth/kakao") || url.startsWith("/api/auth/apple")){
            filterChain.doFilter(req,res);
            return;
        }


        // 쿠키에서 JWT 토큰 빼오기
        String tokenValue = jwtUtil.getTokenFromRequest(req);

        log.info("tokenValue = " + tokenValue);

        // 토큰 존재 시, Authentication 객체 생성하여 인가 완료
        if (StringUtils.hasText(tokenValue)) {
            tokenValue = jwtUtil.substringToken(tokenValue);
            log.info(tokenValue);
            if (!jwtUtil.validateToken(tokenValue)) {
                log.error("Token Error");
                res.setStatus(403);
                res.getWriter().write("토큰이 만료되었습니다.");
                return;
            }
            log.info("Token Validation 완료");
            // Authentication 객체 생성 ( By Username )
            // ( SecurityContextHolder -> SecurityContext -> Authentication 객체 )
            // Authentication 객체 <- UsernamePasswordAuthenticationToken, OAuth2AuthenticationToken
            String username = jwtUtil.getUsername(tokenValue);
            try {
                log.info("setAuthentication");
                setAuthentication(username);
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }
        log.info("chain.doFilter() 호출");
        // JWT 토큰이 존재하지 않으면, 필터 통과!
        filterChain.doFilter(req, res);
    }
    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
    // 인증 객체 생성 ( UserDetails - UsernameAuthentication )
    // ( PrincipalDetails - OAuth2Authentication )
    private Authentication createAuthentication(String username) {
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}