package com.qwertyfox.jsonwebtoken.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

public class MyUsernamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter {


    private final AuthenticationManager authenticationManager;

    public MyUsernamePasswordAuthFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    // The following method authenticates Json request sent using Postman (Body-raw-Json) against the database record
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try{
            MyUsernamePasswordReq requestToAuth = new ObjectMapper()
                    .readValue(request.getInputStream(),MyUsernamePasswordReq.class);
            // The above code maps the request with the class MyUsernamePasswordReq using ObjectMapper()

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    requestToAuth.getUsername(), // Principle/User
                    requestToAuth.getPassword() // Credentials/Password
            );
            // Creates a token to be verified.

            Authentication authUser = authenticationManager.authenticate(auth);
            // Checks the token for username and password and verifies them. Not inline method; to test
            return authUser;

        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    // The following method creates and sends JWT (with secured key) as a header of response to client
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        String securedKey = "This_Need_To_Be_Very_Secured_Key";

        // Following code creates the JWT
        String token = Jwts.builder()
                .setSubject(authResult.getName()) // Username
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new java.util.Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2)))
                .signWith(Keys.hmacShaKeyFor(securedKey.getBytes()))
                .compact(); // Packages into a single line

        response.addHeader("Authorization","Barer " +token); // needs to be in this order


    }
}
