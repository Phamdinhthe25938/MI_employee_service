package com.example.Employee_Service.service.jwt;

import com.obys.common.constant.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service("JWTService")
public class JWTService {

    public boolean validateToken(String token) {
        try {
            return !ObjectUtils.isEmpty(getSubjectFromToken(token)) && !isTokenExpired(token);
        } catch (Exception ex) {
            return false;
        }
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(Constants.AuthService.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(Constants.AuthService.BEARER)) {
            return authHeader.replace(Constants.AuthService.BEARER, "");
        }
        return null;
    }

    public String getSubjectFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().setSigningKey(Constants.AuthService.KEY_PRIVATE).parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String getRole(String token) {
        final Claims claims = Jwts.parser().setSigningKey(Constants.AuthService.KEY_PRIVATE).parseClaimsJws(token).getBody();
        return (String) claims.get(Constants.AuthService.ROLE);
    }
}
