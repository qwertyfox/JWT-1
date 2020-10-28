package com.qwertyfox.jsonwebtoken.jwt;

import com.google.common.base.Strings;
import com.qwertyfox.jsonwebtoken.security.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class MyTokenVerifier extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    @Autowired
    public MyTokenVerifier(JwtConfig jwtConfig, SecretKey secretKey) {
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {


        String authorizationHeader = httpServletRequest.getHeader(jwtConfig.getAuthorizationHeader()); // Header was set in MyUsernamePasswordAuthFilter

        if(authorizationHeader.isEmpty() || authorizationHeader.equals(null) || !authorizationHeader.startsWith("Barer ")) { // "Barer " was set in MyUsernamePasswordAuthFilter
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            // Sending to re verify
            return;
        }

        String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), ""); // Extracting JWT only

        try{

            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();
            String username = body.getSubject();

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    Collections.singleton(new SimpleGrantedAuthority("USER"))
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Checking the validation of the token


        }catch (JwtException e){
            throw new IllegalStateException("Token verification error!");
        }

        filterChain.doFilter(httpServletRequest,httpServletResponse);
        // Sending to the next in line filter in filter chain
    }
}
