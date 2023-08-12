package ourtine.auth.config.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ourtine.auth.filter.JwtAuthorizationFilter;
import ourtine.auth.service.UserDetailsServiceImpl;
import ourtine.auth.utils.JwtUtil;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity(debug = true) // Spring Security 지원을 가능하게 함, 디버깅 모드 활성화 : 어떤 필터를 타는지 로그를 찍어줌.
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured Annotation 활성화.
public class WebSecurityConfig {

    // JWT Provider
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    private static final String[] AUTH_WHITELIST = {
            "/api/auth/**",
            "/swagger-resources",
            "/**swagger**/**",
            "/swagger-resources/**",
            "/csrf/**",
            "/health"
    };


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }



    // FilterChain을 통해 인증, 인가 관리
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                        .csrf().disable()
                        .cors().disable()
                        .formLogin().disable()
                        .logout().disable()
                        .httpBasic().disable()
                        .rememberMe().disable()
                        .authorizeRequests()
                        .antMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated();

        http.addFilterBefore(new JwtAuthorizationFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}