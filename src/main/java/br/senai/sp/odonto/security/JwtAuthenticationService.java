package br.senai.sp.odonto.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtAuthenticationService {

	// Criando um achave secreta e transformando em base64;
	// Pegando os bytes do encoder da palavra
	private static final String SECRET_KEY = Base64.getEncoder().encodeToString("Senai127".getBytes());

	private static final String PREFIX = "Bearer";

	private static final String EMPTY = "";

	private static final String AUTHORIZATION = "Authorization";

	private static final long EXPIRATION_TIME = 86400000;
	
	@Autowired
	private UserDetailServiceImpl userDetailService;

	public String createToken(String username, List<String> roles) {

		// Colocando dentro do payload do token;
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("roles", roles);

		Date now = new Date();
		Date validity = new Date(now.getTime() + EXPIRATION_TIME);

		String token = Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(validity)
				.signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();

		return token;

	}
	

	public Authentication getAuthentication(HttpServletRequest req) {
		
		String token = resolveToken(req);
		
		if(token != null && validateToken(token)) {
			
			// Para pegar o nome do usuário que está no payload do token, no subject;
			String username = Jwts.parser()
					.setSigningKey(SECRET_KEY)
					.parseClaimsJws(token)
					.getBody()
					.getSubject();
			
			if(username != null) {
			
				UserDetails userDetails = userDetailService.loadUserByUsername(username);
				
				return new UsernamePasswordAuthenticationToken(
						userDetails, 
						null, 
						userDetails.getAuthorities()
						);			
			}
		}
		
		return null;
		
	}
	
	
	public boolean validateToken(String token) {
		
		Jws<Claims> claim = Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token);
		
		if(claim.getBody().getExpiration().before(new Date())) {
			return false;
		};
		
		return true;
	}
	

	public String resolveToken(HttpServletRequest req) {
		// Coletando o token do header
		String bearerToken = req.getHeader(AUTHORIZATION);

		if (bearerToken != null && bearerToken.startsWith(PREFIX)) {

			return bearerToken.replace(PREFIX, EMPTY).trim();

		}
		
		return null;
	}

}
