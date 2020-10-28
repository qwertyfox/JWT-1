package com.qwertyfox.jsonwebtoken.security;

import com.google.common.net.HttpHeaders;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "configuration.jwt")
public class JwtConfig {

    private String secretKey;
    private String tokenPrefix;
    private Integer tokenExpiresAfterWeeks;

    public JwtConfig() {
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }


    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public Integer getTokenExpiresAfterWeeks() {
        return tokenExpiresAfterWeeks;
    }

    public void setTokenExpiresAfterWeeks(Integer tokenExpiresAfterWeeks) {
        this.tokenExpiresAfterWeeks = tokenExpiresAfterWeeks;
    }
}
