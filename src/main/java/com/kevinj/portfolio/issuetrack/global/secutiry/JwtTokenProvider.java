package com.kevinj.portfolio.issuetrack.global.secutiry;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider implements TokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-ttl-seconds}")
    private long accessTtlSeconds;

    @Value("${jwt.refresh-ttl-seconds}")
    private long refreshTtlSeconds;

    private SecretKey key;
    private Duration accessTtl;
    private Duration refreshTtl;

    public Duration accessTtl() { return accessTtl; }
    public Duration refreshTtl() { return refreshTtl; }

    @PostConstruct
    protected void init() {
        // secretKey 인코딩 등 필요시
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTtl = Duration.ofSeconds(accessTtlSeconds);
        this.refreshTtl = Duration.ofSeconds(refreshTtlSeconds);
    }

    @Override
    public String createAccessToken(String loginId, Long userId, String role) {
        Claims claims = Jwts.claims().subject(String.valueOf(userId)).build();
        claims.put("login_id", loginId);
        claims.put("role", role);

        Instant now = Instant.now();
        return Jwts.builder()
                .claims(claims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(accessTtl)))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public String createRefreshToken(Long userId) {
        Claims claims = Jwts.claims().subject(String.valueOf(userId)).build();

        Instant now = Instant.now();
        return Jwts.builder()
                .claims(claims)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(refreshTtl)))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public Long getUserId(String token) {
        String userId = parser().parseSignedClaims(token)
                .getPayload()
                .getSubject();
        return Long.parseLong(userId);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            parser().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String getLoginId(String accessToken) {
        Object v =  parser().parseSignedClaims(accessToken)
                .getPayload()
                .get("login_id");
        return v == null ? null : v.toString();
    }

    @Override
    public String getRole(String accessToken) {
        Object v =  parser().parseSignedClaims(accessToken)
                .getPayload()
                .get("role");
        return v == null ? null : v.toString();
    }

    private JwtParser parser() {
        return Jwts.parser().verifyWith(key).build();
    }

}
