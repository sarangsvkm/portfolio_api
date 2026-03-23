package com.sarangsvkm.portfolio_api.config;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sarangsvkm.portfolio_api.apiuser.ApiUserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AdminAuthFilter extends OncePerRequestFilter {

    private final ApiUserService apiUserService;

    public AdminAuthFilter(ApiUserService apiUserService) {
        this.apiUserService = apiUserService;
    }

    @Override
    @SuppressWarnings("null")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // 🛡️ Admin Protection logic:
        boolean isApiWrite = path.contains("/api/") && !path.contains("/api/auth/") && !method.equalsIgnoreCase("GET");
        boolean isConfigAccess = path.contains("/api/config");
        boolean isContactReport = path.contains("/api/contact/report");

        if (isApiWrite || isConfigAccess || isContactReport) {

            String username = request.getHeader("X-Admin-Username");
            String password = request.getHeader("X-Admin-Password");

            if (username == null || password == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Missing authentication headers (X-Admin-Username, X-Admin-Password)");
                return;
            }

            try {
                apiUserService.login(username, password);
            } catch (RuntimeException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid credentials: " + e.getMessage());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
