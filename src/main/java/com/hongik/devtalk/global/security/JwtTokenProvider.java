package com.hongik.devtalk.global.security;

import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.security.Key;
import java.util.Collections;
import java.util.Date;

import static com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode.INVALID_TOKEN;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final long ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 7; // 7일
    // 1000L * 60 * 30;     // 30분
    private final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 7; // 7일

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
    }

    public String generateToken(Authentication authentication) {
        String loginId = authentication.getName();

        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_ADMIN");

        long now = System.currentTimeMillis();
        //long expMs = jwtProperties.getExpiration().getAccess();

        return Jwts.builder()
                .setSubject(loginId)
                .claim("role", role)
                .claim("type","access_token")
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + ACCESS_TOKEN_VALID_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        String loginId = authentication.getName();

        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(loginId)
                .claim("type","refresh_token")
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + REFRESH_TOKEN_VALID_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String loginId = claims.getSubject();
        String role = claims.get("role", String.class);
        if (!StringUtils.hasText(role)) {
            role = "ROLE_ADMIN"; // 혹은 예외 던지기
        }

        User principal = new User(loginId, "",Collections.singleton(new SimpleGrantedAuthority(role)));
        return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
    }

    public static String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(Constants.AUTH_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(Constants.TOKEN_PREFIX)) {
            return bearerToken.substring(Constants.TOKEN_PREFIX.length());
        }
        return null;
    }

    public Authentication extractAuthentication(HttpServletRequest request){
        String accessToken = resolveToken(request);
        if(accessToken == null || !validateToken(accessToken)) {
            throw new GeneralException(INVALID_TOKEN);
        }
        return getAuthentication(accessToken);
    }


}
