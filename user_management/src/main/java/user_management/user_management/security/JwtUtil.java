<<<<<<< HEAD
package user_management.user_management.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
=======
/*package user_management.user_management.security;
>>>>>>> 9c55969568590acf3b1ae886fba49b8f2732e882

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

<<<<<<< HEAD
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
=======
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import user_management.user_management.entity.Permission;

import java.util.*;
import java.util.stream.Collectors;
>>>>>>> 9c55969568590acf3b1ae886fba49b8f2732e882

@Component
public class JwtUtil {

<<<<<<< HEAD
    private static final String SECRET = "my-secure-secret-key-for-jwt-must-be-32+chars"; // minimum 32 chars
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
=======
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
>>>>>>> 9c55969568590acf3b1ae886fba49b8f2732e882
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

<<<<<<< HEAD
    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
=======
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
>>>>>>> 9c55969568590acf3b1ae886fba49b8f2732e882
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

<<<<<<< HEAD
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
        String username = extractUsername(token); // method that parses the token and gets the subject (username)
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    } catch (ExpiredJwtException e) {
        System.out.println("Token expired");
    } catch (JwtException e) {
        System.out.println("Token is invalid: " + e.getMessage());
    } catch (Exception e) {
        System.out.println("Unknown error validating token: " + e.getMessage());
    }
    return false;
=======
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
>>>>>>> 9c55969568590acf3b1ae886fba49b8f2732e882
    }
}
