package practicerestapi.jwt;

import java.util.Date;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import practicerestapi.account.Account;
import practicerestapi.account.AccountAdapter;
import practicerestapi.account.AccountService;

@Slf4j
@Component
public class JwtProvider {

	 public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	 
	 @Value("${jwt.secret}")
	 private String secret;
	 
	 public String generateJwtToken(Authentication authentication) {
		 
		 AccountAdapter accountAdapter = (AccountAdapter) authentication.getPrincipal();
		 Account account = accountAdapter.getAccount();
	     return Jwts.builder()
	                .setSubject((account.getEmail()))
	                .setIssuedAt(new Date(System.currentTimeMillis()))
	                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
	                .signWith(SignatureAlgorithm.HS512, secret)
	                .compact();
	 }
	 
	 public String getUserNameFromJwtToken(String token) {
	        return Jwts.parser()
	                      .setSigningKey(secret)
	                      .parseClaimsJws(token)
	                      .getBody().getSubject();
	 }
	 
	 
	 public boolean validateJwtToken(String authToken) {
	        try {
	            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
	            return true;
	        } catch (SignatureException e) {
	            log.error("Invalid JWT signature -> Message: {} ", e);
	        } catch (MalformedJwtException e) {
	        	log.error("Invalid JWT token -> Message: {}", e);
	        } catch (ExpiredJwtException e) {
	        	log.error("Expired JWT token -> Message: {}", e);
	        } catch (UnsupportedJwtException e) {
	        	log.error("Unsupported JWT token -> Message: {}", e);
	        } catch (IllegalArgumentException e) {
	        	log.error("JWT claims string is empty -> Message: {}", e);
	        }
	        
	        return false;
	    }
	 
}
