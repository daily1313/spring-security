package com.example.basicauth.config;

import com.example.basicauth.authentication.CustomAuthenticationFilter;
import com.example.basicauth.authentication.CustomAuthenticationProvider;
import com.example.basicauth.authentication.CustomUserDetailsService;
import com.example.basicauth.handler.CustomAccessDeniedHandler;
import com.example.basicauth.handler.CustomAuthenticationEntryPoint;
import com.example.basicauth.repository.MemberRepository;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST = {
            "/api/v1/auth/login",
            "/api/v1/auth/signup"
    };

    @Bean
    public SecurityFilterChain basicAuthFilterChain(final HttpSecurity http,
                                                    final CustomAuthenticationFilter authenticationFilter,
                                                    final CustomAuthenticationEntryPoint authenticationEntryPoint,
                                                    final CustomAccessDeniedHandler accessDeniedHandler) throws Exception {
        http
                .headers(x -> x.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionConfig -> exceptionConfig
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(final HttpSecurity http,
                                                       final AuthenticationProvider customAuthenticationProvider) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(customAuthenticationProvider)
                .build();
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter(final AuthenticationManager authenticationManager) {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter(new AntPathRequestMatcher("/api/v1/auth/login", "POST"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(final CustomUserDetailsService customUserDetailsService,
                                                               final PasswordEncoder passwordEncoder) {
        return new CustomAuthenticationProvider(customUserDetailsService, passwordEncoder);
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService(final MemberRepository memberRepository) {
        return new CustomUserDetailsService(memberRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
