package com.example.dashboardbackend.security.jwt;

import com.example.dashboardbackend.models.Family;
import com.example.dashboardbackend.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class JwtUtils {
    @Value("${spring.app.jwtSecret}")
    private String SECRET_KEY;

    @Value("${spring.app.jwtExpirationMs}")
    private long EXPIRATION_DATE;

    public String extractUsername(String jwt) {
        return extractAllClaims(jwt).getSubject();
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();

    }

    public String generateFamilyToken(Family family){
        return Jwts.builder()
                .subject(family.getFamilyName())
                .claim("type", "FAMILY")
                .claim("familyId", family.getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 900000))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateUserToken(User user){
        JwtBuilder builder = Jwts.builder()
                .subject(user.getName()) // Subject für extractUsername
                .claim("type", "USER")
                .claim("userId", user.getId())
                .claim("role", user.getUserRole().name());

        if(user.getFamily() != null){
            builder.claim("familyId", user.getFamily().getId());
        }

        return builder
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        final String username = extractUsername(jwt);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwt);
    }

    public boolean isValidFamilyToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return "FAMILY".equals(claims.get("type", String.class))
                    && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public Long extractFamilyId(String token) {
        return extractAllClaims(token).get("familyId", Long.class);
    }

    public boolean isTokenExpired(String jwt) {
        return extractAllClaims(jwt).getExpiration().before(new Date());
    }

    public Claims extractAllClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractFromCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) return null;
        for (var cookie : request.getCookies()) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
