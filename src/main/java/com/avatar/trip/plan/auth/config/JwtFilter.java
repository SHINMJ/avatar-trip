package com.avatar.trip.plan.auth.config;

import com.avatar.trip.plan.exception.TokenValidationException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTH_ENDPOINT_PREFIX = "/auth/";

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().startsWith(AUTH_ENDPOINT_PREFIX)){
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolveToken(request);
        log.debug("token = {}", token);

        if (token == null){
            sendError(response, "토큰이 없습니다.");
            return;
        }

        try {
            setAuthentication(token);
        }catch (TokenValidationException e) {
            sendError(response, e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);

    }

    private void setAuthentication(String token){
        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private String resolveToken(HttpServletRequest request){
        String bearer = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearer) && bearer.startsWith(BEARER_PREFIX)){
            return bearer.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        SecurityContextHolder.clearContext();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
    }
}
