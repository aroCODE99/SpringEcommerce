package com.aro.SpringEcommerce.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil tokenHelper;

    @Autowired
    private UserDetailsService userDetailsServiceImpl;

    private String getToken( HttpServletRequest request ) {

        String authHeader = request.getHeader("Authorization");
        if ( authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String authToken = getToken( request );
        log.warn(request.getRequestURI());
        if (authToken != null) {
            // Get username from token
            String username = tokenHelper.getUsernameFromToken( authToken );
            if ( username != null && SecurityContextHolder.getContext().getAuthentication() == null ) {

                // Get user
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

                // Create authentication
                TokenBasedAuthentication authentication = new TokenBasedAuthentication( userDetails );
                authentication.setToken(authToken);
                SecurityContextHolder.getContext().setAuthentication( authentication );
            }
        }
        chain.doFilter(request, response);
    }

}
