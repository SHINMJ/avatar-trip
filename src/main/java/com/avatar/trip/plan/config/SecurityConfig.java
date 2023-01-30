package com.avatar.trip.plan.config;

import com.avatar.trip.plan.auth.config.JwtAccessDeniedHandler;
import com.avatar.trip.plan.auth.config.JwtAuthenticationEntryPoint;
import com.avatar.trip.plan.auth.config.JwtSecurityConfig;
import com.avatar.trip.plan.auth.config.TokenProvider;
import com.avatar.trip.plan.user.domain.UserRepository;
import com.avatar.trip.plan.user.infra.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String tokenSecret;
    @Value("${jwt.expired-in}")
    private long expiredIn;
    @Value("${jwt.refresh-expired-in}")
    private long refreshExpiredIn;

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;
    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
        throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public TokenProvider tokenProvider(){
        return new TokenProvider(tokenSecret, expiredIn, refreshExpiredIn, new CustomUserDetailService(userRepository));
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring()
            .antMatchers("/css/**", "/js/**", "/favicon/ico");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http.csrf().disable()
            .httpBasic()
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
            .and()
            .authorizeHttpRequests()
                .mvcMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .apply(new JwtSecurityConfig(tokenProvider()));

        return http.build();
    }
}
