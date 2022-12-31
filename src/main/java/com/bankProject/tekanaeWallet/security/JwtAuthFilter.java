package com.bankProject.tekanaeWallet.security;

import com.bankProject.tekanaeWallet.utils.JwtUserDetailService;
import com.bankProject.tekanaeWallet.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    private JwtUserDetailService jwtUserDetailService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        // check if request header contains authorization bearer token
        String header = request.getHeader("Authorization");
        String jwtToken = null;
        String userEmail = null;
        // check if header has keyword 'bearer' in it
        if(header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwtToken = header.split(" ")[1];
        // extracting payload from token
        try{
            // ...
            userEmail = jwtUtil.getUserNameFromToken(jwtToken);
        } catch (IllegalArgumentException i) {
            throw new IllegalArgumentException("Unable to get token");
        } catch (ExpiredJwtException e) {
            try {
                throw new Exception("JWT has expired");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = jwtUserDetailService.loadUserByUsername(userEmail);
            if(jwtUtil.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);

    }
}
