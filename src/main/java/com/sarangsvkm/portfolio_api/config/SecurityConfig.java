package com.sarangsvkm.portfolio_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import com.sarangsvkm.portfolio_api.entity.SystemConfig;
import com.sarangsvkm.portfolio_api.repository.SystemConfigRepository;

@Configuration
public class SecurityConfig {

    private final SystemConfigRepository configRepo;
    private final AdminAuthFilter adminAuthFilter;

    public SecurityConfig(SystemConfigRepository configRepo, AdminAuthFilter adminAuthFilter) {
        this.configRepo = configRepo;
        this.adminAuthFilter = adminAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // ✅ Fetch allowed origins from database, with localhost fallbacks
        String rawOrigins = configRepo.findByConfigKey("cors.allowed-origins")
                .map(SystemConfig::getConfigValue)
                .orElse("http://localhost,http://localhost:3000,http://localhost:5173,http://localhost:5174,http://localhost:8080,http://127.0.0.1,http://127.0.0.1:3000,http://127.0.0.1:5173,http://127.0.0.1:5174,http://192.168.68.54,http://192.168.68.54:3000,http://192.168.68.54:5173,http://192.168.68.54:5174");

        config.setAllowedOrigins(Arrays.asList(rawOrigins.split(",")));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // 👋 EXPLICITLY allow ALL headers to avoid 403 Forbidden with custom headers
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));
        
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 🚨 Register for ALL paths to be sure
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().permitAll())
                // 🔐 Add our Custom Filter after CORS and BEFORE any Auth logic
                .addFilterBefore(adminAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}