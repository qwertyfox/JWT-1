package com.qwertyfox.jsonwebtoken.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class MyTokenVerifier extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {


        String authorizationHeader = httpServletRequest.getHeader("Authorization"); // Header was set in MyUsernamePasswordAuthFilter

        if(authorizationHeader.isEmpty() || authorizationHeader.equals(null) || !authorizationHeader.startsWith("Barer ")) { // "Barer " was set in MyUsernamePasswordAuthFilter
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            // Sending to re verify
            return;
        }

        String token = authorizationHeader.replace("Barer ", ""); // Extracting JWT only

        try{
            String securedKey = "This_Need_To_Be_Very_Secured_Key";

            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(securedKey.getBytes()))
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


            System.out.println("Token was authenticated.");


        }catch (JwtException e){
            throw new IllegalStateException("Token verification error!");
        }

        filterChain.doFilter(httpServletRequest,httpServletResponse);
        // Sending to the next in line filter in filter chain
    }
}
