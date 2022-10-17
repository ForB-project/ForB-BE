package com.innovationcamp.finalprojectforb.security;

import com.innovationcamp.finalprojectforb.jwt.JwtAccessDeniedHandler;
import com.innovationcamp.finalprojectforb.jwt.JwtAuthenticationEntryPoint;
import com.innovationcamp.finalprojectforb.jwt.TokenProvider;
import com.innovationcamp.finalprojectforb.model.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
public class WebSecurityConfig {

    @Value("${jwt.secret}")
    String SECRET_KEY;

    private final UserDetailsServiceImpl userDetailsService;
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // "/h2-console/**" -> 이 url 경로 : h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        return (web) -> web.ignoring().antMatchers("/h2-console/**", "/favicon.ico");
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource);

        // CSRF 설정 Disable
        http.csrf().disable();
        // exception handling 할 때 우리가 만든 클래스를 추가
        http
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // jwt 자체에 문제가 있는거
                .accessDeniedHandler(jwtAccessDeniedHandler); // jwt는 정상인데 접근하려는 url의 권한이 다를 때

        // 서버에서 인증은 JWT로 인증하기 때문에 Session의 생성을 막습니다.
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/api/member/**").permitAll()
                .antMatchers("/api/oauth2/**").permitAll()
                .antMatchers("/login/oauth2/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/api/roadmap/**").permitAll()
                .antMatchers("/stomp/**").permitAll()
                .antMatchers("/pub/**").permitAll()
                .antMatchers("/sub/**").permitAll()
                .antMatchers("/chat/**").permitAll()
                .anyRequest().authenticated();

        // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
        http
                .apply(new JwtSecurityConfig(SECRET_KEY, tokenProvider, userDetailsService));
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

}
