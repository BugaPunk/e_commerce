package com.bugabuga.e_commerce.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {
    
    private String secretKey = "defaultSecretKeyThatShouldBeChangedInProduction";
    private long expirationMs = 86400000; // 24 horas por defecto
    private String tokenPrefix = "Bearer ";
    private String headerString = "Authorization";
    
    public String getSecretKey() {
        return secretKey;
    }
    
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
    
    public long getExpirationMs() {
        return expirationMs;
    }
    
    public void setExpirationMs(long expirationMs) {
        this.expirationMs = expirationMs;
    }
    
    public String getTokenPrefix() {
        return tokenPrefix;
    }
    
    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }
    
    public String getHeaderString() {
        return headerString;
    }
    
    public void setHeaderString(String headerString) {
        this.headerString = headerString;
    }
}
