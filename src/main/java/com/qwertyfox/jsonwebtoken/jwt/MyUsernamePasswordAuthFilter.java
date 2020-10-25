package com.qwertyfox.jsonwebtoken.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyUsernamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter {


    private final AuthenticationManager authenticationManager;

    public MyUsernamePasswordAuthFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

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

            Authentication authUser = authenticationManager.authenticate(auth);
            // Checks if the username and passwords matches. Not inline method; to test
            return authUser;

        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }
}
