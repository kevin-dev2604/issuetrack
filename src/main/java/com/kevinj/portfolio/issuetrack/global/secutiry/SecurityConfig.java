package com.kevinj.portfolio.issuetrack.global.secutiry;

import com.kevinj.portfolio.issuetrack.global.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;
//    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**").permitAll()
                        .requestMatchers("/users/signup", "/auth/**").permitAll()
                        .requestMatchers("/admin/**").hasRole(UserRole.ADMIN.name())
                        .requestMatchers("/process/**", "/issue/**", "/dilemma/user/*").hasRole(UserRole.USER.name())
                        .requestMatchers(HttpMethod.GET, "/dilemma/search").permitAll()
                        .requestMatchers(
                                regexMatcher(HttpMethod.GET, "^/dilemma/[0-9]+$")
                        ).permitAll()
                        .requestMatchers(
                                regexMatcher(HttpMethod.POST, "^/dilemma/[0-9]+/close$")
                        ).hasRole(UserRole.DILEMMA.name())
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .userDetailsService(userDetailsService);

        // JWT 필터를 UsernamePasswordAuthenticationFilter 전에 추가
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
