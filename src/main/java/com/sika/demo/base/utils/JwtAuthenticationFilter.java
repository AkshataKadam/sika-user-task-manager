package com.sika.demo.base.utils;

import com.sika.demo.base.exception.ApiException;
import com.sika.demo.base.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
                || path.startsWith("/h2-console")
                || (referer != null && referer.contains("swagger"))) {

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            "swagger-user",
                            null,
                            List.of()
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
            return;
        }

        String token = null;
        final String authHeader = request.getHeader("Authorization");
        logger.info("authHeader " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            logger.info("token " + token);
        }

        if (!token.equals("null") && !token.equals("abc")) {
            logger.info("token is not null: " + token);
            authenticateToken(request, token);
            authenticated = true;
        } else {
            authenticated = false;
        }

        logger.info("authenticated: " + authenticated);

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
            }
        }
    }
}
