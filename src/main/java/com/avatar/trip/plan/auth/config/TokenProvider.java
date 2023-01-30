package com.avatar.trip.plan.auth.config;

import com.avatar.trip.plan.exception.TokenValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Slf4j
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String DELIMITER = ",";


    private final String tokenSecret;
    private final long expiredIn;
    private final long refreshExpiredIn;
    private final UserDetailsService userDetailsService;

    private Key key;


    public TokenProvider(String tokenSecret, long expiredIn, long refreshExpiredIn, UserDetailsService userDetailsService) {
        this.tokenSecret = tokenSecret;
        this.expiredIn = expiredIn;
        this.refreshExpiredIn = refreshExpiredIn;
        this.userDetailsService = userDetailsService;
        byte[] keyBytes = Decoders.BASE64.decode(this.tokenSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Authentication authentication){
        return createToken(authentication, expiredIn);
    }

    public String createRefreshToken(Authentication authentication){
        return createToken(authentication, refreshExpiredIn);
    }

    public Authentication getAuthentication(String token){
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public boolean validateToken(String token){
        try{
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 서명입니다.");
            throw new TokenValidationException("잘못된 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 토큰 입니다.");
            throw new TokenValidationException("만료된 토큰 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 토큰입니다.");
            throw new TokenValidationException("지원되지 않는 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("토큰이 잘못되었습니다.");
            throw new TokenValidationException("토큰이 잘못되었습니다.");
        }

    }

    private String createToken(Authentication authentication, long expired){
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(DELIMITER));

        Date now = new Date();
        Date validity = new Date(now.getTime() + expired);

        return Jwts.builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(key)
            .setExpiration(validity)
            .compact();
    }
}
