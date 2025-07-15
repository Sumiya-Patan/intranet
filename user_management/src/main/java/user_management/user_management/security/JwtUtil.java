/*package user_management.user_management.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import user_management.user_management.entity.Permission;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "your_jwt_secret";

    public String generateToken(UserDetails userDetails, Set<Permission> permissions) {
        List<String> permissionList = permissions.stream()
                .map(p -> p.getResource() + ":" + p.getAction())
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("permissions", permissionList)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public List<String> extractPermissions(String token) {
        Claims claims = extractClaims(token);
        return claims.get("permissions", List.class);
    }
}
*/
package user_management.user_management.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import user_management.user_management.entity.Permission;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "your_jwt_secret"; // TODO: Move to environment/config

    /**
     * Generates JWT token containing username and a list of authorities (permissions)
     */
    public String generateToken(UserDetails userDetails, Set<Permission> permissions) {
        List<String> permissionList = permissions.stream()
                .map(p -> p.getResource() + ":" + p.getAction())
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("permissions", permissionList)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * Extracts username (subject) from JWT token
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * Validates JWT token by matching username and checking expiration
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks if the token is expired
     */
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    /**
     * Extracts all claims from token
     */
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Optionally extract embedded permissions from the token
     */
    @SuppressWarnings("unchecked")
    public List<String> extractPermissions(String token) {
        Claims claims = extractClaims(token);
        return (List<String>) claims.get("permissions", List.class);
    }
}
