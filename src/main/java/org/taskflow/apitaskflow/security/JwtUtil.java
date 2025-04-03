package org.taskflow.apitaskflow.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
	
	@Value("${jwt.secret}")
	private String jwtSecret;
	
	@Value("${jwt.expirationMs}")
	private int jwtExpirationMs;
	
	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}
	
	public String generateJwtToken(String username) {
		return Jwts.builder()
				.subject(username)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
				.signWith(getSigningKey(), SignatureAlgorithm.HS512)
				.compact();
	}
	
	public String getUsernameFromJwtToken(String token) {
		return Jwts.parser()
				.setSigningKey(getSigningKey())
//				.verifyWith(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	public boolean validateJwtToken(String token) {
		try {
			Jwts.parser()
					.setSigningKey(getSigningKey())
					.build()
					.parseClaimsJws(token);
			return true;
		} catch (JwtException e) {
			// Log de l'exception si nécessaire
		}
		return false;
	}
}
