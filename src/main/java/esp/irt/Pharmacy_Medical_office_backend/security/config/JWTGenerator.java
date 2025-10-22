package esp.irt.Pharmacy_Medical_office_backend.security.config;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTGenerator {

    public Map<String, String> generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);
        Date expireRefreshDate = new Date(currentDate.getTime() + SecurityConstants.JWT_REFRESH_EXPIRATION);
        List<String> roles = authentication.getAuthorities()
        	    .stream()
        	    .map(GrantedAuthority::getAuthority)
        	    .collect(Collectors.toList());

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .claim("roles",roles)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_SECRET)
                .compact();
        
        String refreshToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireRefreshDate)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_SECRET)
                .compact();
        
        Map<String, String> idToken = new HashMap<>();
        idToken.put("access-token", token);
        idToken.put("refresh-token", refreshToken);
        idToken.get("refresh-token");
        return idToken;
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SecurityConstants.JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
        }
    }
}
