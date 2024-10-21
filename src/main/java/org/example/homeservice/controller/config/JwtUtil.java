package org.example.homeservice.controller.config;

import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class JwtUtil {



    private String SECRET_KEY = "mykeaskdjfhlasdjflaksdjfasdfasdfasdfasdfjjjjjjjasdfasjdlfjaslkdjfalsdfasdfasdfasdfsadfy";
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

//    public String generateToken(UserDetails userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//        // Add role with the prefix "ROLE_"
//        String role = userDetails.getAuthorities().stream()
//                .findFirst()
//                .get()
//                .getAuthority(); // e.g., "ROLE_CUSTOMER"
//
//        claims.put("roles", role); // Store "ROLE_CUSTOMER" instead of just "CUSTOMER"
//        return doGenerateToken(claims, userDetails.getUsername());
//    }

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
    }