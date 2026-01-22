package com.gdg.backend.global.jwt;

import com.gdg.backend.global.security.SignupPrincipal;
import com.gdg.backend.global.security.UserPrincipal;
import com.gdg.backend.user.domain.OauthProvider;
import com.gdg.backend.user.domain.User;
import com.gdg.backend.user.domain.UserStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider {

    private final SecretKey secretKey;

    @Getter
    private final long accessTokenValidityMillis;

    @Getter
    private final long refreshTokenValidityMillis;

    private final long signupTokenValidityMillis;

    public TokenProvider(JwtProperties jwtProperties) {
        this.secretKey = Keys.hmacShaKeyFor(
                jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)
        );

        this.accessTokenValidityMillis = jwtProperties.getAccessTokenValidity();
        this.refreshTokenValidityMillis = jwtProperties.getRefreshTokenValidity();
        this.signupTokenValidityMillis = jwtProperties.getSignupTokenValidity();

        log.info("JWT key initialized. key length = {}", secretKey.getEncoded().length);
    }

    private String createToken(String subject, Claims claims, long validityMillis) {
        long now = System.currentTimeMillis();
        Date expiryDate = new Date(now + validityMillis);

        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuedAt(new Date(now))
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public String accessToken(User user) {
        Claims claims = Jwts.claims()
                .add("role", user.getRole().name())
                .build();

        return createToken(user.getId().toString(), claims, accessTokenValidityMillis);
    }

    public String refreshToken(User user) {
        return createToken(user.getId().toString(), Jwts.claims().build(), refreshTokenValidityMillis);
    }

    public String authToken(OauthProvider provider, String providerId, String email) {
        Claims claims = Jwts.claims()
                .add("provider", provider.name())
                .add("providerId", providerId)
                .add("email", email)
                .add("status", UserStatus.PENDING.name())
                .build();

        return createToken("OAUTH_SIGNUP", claims, signupTokenValidityMillis);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        String role = claims.get("role", String.class);
        if (role == null) {
            throw new IllegalStateException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(role.split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserPrincipal principal =
                new UserPrincipal(Long.valueOf(claims.getSubject()), role);

        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

    public SignupPrincipal parseSignupToken(String token) {
        Claims claims = parseClaims(token);

        if (!"OAUTH_SIGNUP".equals(claims.getSubject())) {
            throw new IllegalStateException("Signup Token 아님");
        }

        if (!UserStatus.PENDING.name().equals(claims.get("status"))) {
            throw new IllegalStateException("유효하지 않은 Signup 상태");
        }

        return new SignupPrincipal(
                OauthProvider.valueOf(claims.get("provider", String.class)),
                claims.get("providerId", String.class),
                claims.get("email", String.class)
        );
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.debug("JWT expired");
            return false;
        } catch (Exception e) {
            log.debug("Invalid JWT", e);
            return false;
        }
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String revokeToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
