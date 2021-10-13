package com.johnwstump.incentivizer.security.web.jwt;

import com.johnwstump.incentivizer.model.user.IUser;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import java.util.Date;

import static java.lang.String.format;

@CommonsLog
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private final String jwtSecret = "92B02807B1686BE9EDE5BFD1F8A8D46BA01ACF4352EE30AC625CC563B446B927";
    private final String jwtIssuer = "johnwstump.com";

    // 3 Hours
    private final int EXPIRATION_MS = 1000 * 60 * 60 * 3;

    public String generateAccessToken(IUser user) {
        return Jwts.builder()
                .setSubject(format("%s,%s", user.getId(), user.getEmail()))
                .setIssuer(jwtIssuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Long getUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject().split(",")[0]);
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject().split(",")[1];
    }

    public Date getExpirationDate(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature - {}" + ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token - {}" + ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token - {}" + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token - {}" + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty - {}" + ex.getMessage());
        }
        return false;
    }

}