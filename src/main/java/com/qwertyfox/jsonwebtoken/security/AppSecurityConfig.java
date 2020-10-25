package com.qwertyfox.jsonwebtoken.security;

import com.qwertyfox.jsonwebtoken.appUser.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final MyUserDetailsService myUserDetailsService;

    @Autowired
    public AppSecurityConfig(PasswordEncoder passwordEncoder, MyUserDetailsService myUserDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.myUserDetailsService = myUserDetailsService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(myUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//    }

}
