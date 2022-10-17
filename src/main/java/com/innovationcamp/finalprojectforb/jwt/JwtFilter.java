package com.innovationcamp.finalprojectforb.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovationcamp.finalprojectforb.dto.ResponseDto;
import com.innovationcamp.finalprojectforb.enums.ErrorCode;
import com.innovationcamp.finalprojectforb.model.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static String AUTHORIZATION_HEADER = "Authorization";
    public static String BEARER_PREFIX = "Bearer ";
    public static String AUTHORITIES_KEY = "auth";
    private final String SECRET_KEY;
    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    // 실제 필터링 로직은 doFilterInternal 에 들어감
    // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        // 1. Request Header 에서 토큰을 꺼냄
        String accessToken = resolveToken(request);

        // 2. validateToken 으로 토큰 유효성 검사
        // 정상 토큰이면 해당 토큰으로 Authentication 을 가져와서 SecurityContext 에 저장
        if (StringUtils.hasText(accessToken)) {
            Claims claims;
            try {
                claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken)
                        .getBody();
            } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {

                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().println(
                        new ObjectMapper().writeValueAsString(
                                new ResponseDto<>(null, ErrorCode.BAD_TOKEN)));
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;

            } catch (ExpiredJwtException e) {

                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().println(
                        new ObjectMapper().writeValueAsString(
                                new ResponseDto<>(null, ErrorCode.EXPIRED_TOKEN)));
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;

            } catch (UnsupportedJwtException e) {

                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().println(
                        new ObjectMapper().writeValueAsString(
                                new ResponseDto<>(null, ErrorCode.WRONG_TOKEN)));
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;

            }

            String subject = claims.getSubject();
            UserDetails principal = userDetailsService.loadUserByUsername(subject);

            // DB의 member의 role을 가져옴
            Collection<? extends GrantedAuthority> repoMemberRole = principal.getAuthorities();

            // jwt의 role을 가져옴
            String jwtRole = claims.get(AUTHORITIES_KEY).toString();

            boolean checkRole = false;
            for (GrantedAuthority role : repoMemberRole) {
                if (jwtRole.equals(role.getAuthority())) {
                    checkRole = true;
                }
            }

            // db의 role과 jwt의 role 일치하지 않을 때
            if (!checkRole) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().println(
                        new ObjectMapper().writeValueAsString(
                                new ResponseDto<>(null, ErrorCode.NOT_SAME_AUTHORITY)));
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(jwtRole);
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(authority);

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response); // 시큐리티 체인이기 때문에 해당 메소드 종료 시에는 이 명령어를 사용해야한다.
    }

    // Request Header 에서 토큰 정보를 꺼내오기
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }

        return null;
    }

}

