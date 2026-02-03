package com.sika.demo.base.utils;


import com.sika.demo.base.exception.ApiException;
import com.sika.demo.base.model.AuthResponse;
import com.sika.demo.base.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService authService;

    boolean authenticated = false;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        String referer = request.getHeader("Referer");

        if (path.startsWith("/auth")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || (referer != null && referer.contains("swagger"))) {

//            UsernamePasswordAuthenticationToken authentication =
//                    new UsernamePasswordAuthenticationToken(
//                            "swagger-user",
//                            null,
//                            List.of()
//                    );
//            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
            return;
        }

        String token = null;
//        String refreshToken = null;
        final String authHeader = request.getHeader("Authorization");
        logger.info("authHeader" + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            logger.info("token " + token);
        }

//        if (request.getCookies() != null) {
//            for (Cookie cookie : request.getCookies()) {
//                if ("REFRESH_TOKEN".equals(cookie.getName())) {
//                    refreshToken = cookie.getValue();
//                }
//            }
//        }
        logger.info("token " + token.length());

        if (token.equals("null")) {
            logger.info("token yayy " + token);
        }
        if (!token.equals("null") && !token.equals("abc")) {
            logger.info("token is not null: " + token);
            authenticateToken(request, token);
            authenticated = true;
        } else {
            authenticated = false;
        }

        logger.info("authenticated: " + authenticated);
//        logger.info("refreshToken: " + refreshToken);

//        if (!authenticated && refreshToken != null) {
//            try {
//                AuthResponse newTokens = authService.refreshToken(refreshToken, response);
//                String newAccessToken = newTokens.getToken();
//                logger.info("newAccessToken: " + newAccessToken);
//                authenticateToken(request, newAccessToken);
//            } catch (RuntimeException e) {
//                logger.warn("Refresh token invalid or expired: {}");
//                authenticated = false;
//
//                // Optionally clear refresh cookie here
//                Cookie cookie = new Cookie("REFRESH_TOKEN", null);
//                cookie.setHttpOnly(true);
//                cookie.setMaxAge(0);
//                cookie.setPath("/");
//
//                response.addCookie(cookie);
//            }
//        } else if (authenticated && refreshToken == null) {
//            logger.error("Refresh token invalid or expired: {}");
//            throw new ApiException("REFRESH_TOKEN_EXPIRED", "Refresh token expired, please re-login", 403);
//        }
            if (!authenticated) {
                logger.error("Please Re-Login");
                throw new ApiException("REFRESH_TOKEN_EXPIRED", "Refresh token expired, please re-login", 403);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateToken(HttpServletRequest request, String token) {
        String username = jwtUtil.extractUsername(token);
        logger.info(username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null,
                                userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
//                authenticated = true;
            }
        }
    }
}
