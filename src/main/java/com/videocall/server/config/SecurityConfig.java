package com.videocall.server.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig {
    UserDetailsService userDetailsService;

    private static String[] PUBLIC_ENDPOINTS = {
            "/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cor -> cor.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(
                        author -> author
                                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(
                        form -> form
                                .loginProcessingUrl("/login")
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .defaultSuccessUrl("http://localhost:3000/html/index.html", true) // Đường dẫn frontend sau khi đăng nhập thành công
                                .permitAll()
                )
                .logout(
                        log -> log.invalidateHttpSession(true)
                                .clearAuthentication(true)
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://127.0.0.1:5500",
                "http://localhost:3000",
                "http://localhost:3001",
                "https://localhost:3001",
                "https://localhost:3000",
                "https://backend-production-c2cb.up.railway.app"));

        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
