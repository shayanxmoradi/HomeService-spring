package org.example.homeservice.controller.config;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class JwtUtil {



//    private String SECRET_KEY = "mykeaskdjfhlasdjflaksdjfasdfasdfasdfasdfjjjjjjjasdfasjdlfjaslkdjfalsdfasdfasdfasdfsadfy";

    @Value("${secret-key}")
    private String SECRET_KEY;
    private long expirationTime = 86400000; // 1 day

        public String generateToken(String username,String roles) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("roles", roles);

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                    .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                    .compact();
        }


        public Boolean validateToken(String token, String username) {
            final String extractedUsername = extractUsername(token);
            return (extractedUsername.equals(username) && !isTokenExpired(token));
        }

        public String extractUsername(String token) {
            return extractAllClaims(token).getSubject();
        }

        private Claims extractAllClaims(String token) {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        }

        private Boolean isTokenExpired(String token) {
            return extractAllClaims(token).getExpiration().before(new Date());
        }
    public String getCurrentUserEmail(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null) {
            return  extractUsername(token);
        }
        throw new RuntimeException("User not authenticated");
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
    }