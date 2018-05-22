package com.adidas.cloud.security.jwt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

/**
 * TokenAuthenticationService.
 * It handles the creation and validation of the JWT token.
 * @author nestis
 *
 */
@Service
@Slf4j
public class TokenAuthenticationService {

	@Value("${security.maxLength:360000}")
	private long maxLength;

	@Value("${security.secretKey:MagicJohnson}")
	private String secretKey;

	@Value("${security.tokenPrefix:Bearer}")
	private String tokenPrefix;

	@Value("${security.header:Authorization}")
	private String header;

	/**
	 * Creates a JWT token from the Authentication object passed as parameter and adds it to the request as a header.
	 * @param res Request object.
	 * @param auth Authentication object.
	 */
	public void addAuthentication(HttpServletResponse res, Authentication auth) {
		log.debug("Creating JWT for user {}", auth.getName());

		// Create claims with username and authorities
		Claims claims = Jwts.claims().setSubject(auth.getName());
		claims.put("authorities", auth.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList()));

		// Create JWT token
		String JWT = Jwts.builder().setClaims(claims).setExpiration(new Date(System.currentTimeMillis() + maxLength))
				.signWith(SignatureAlgorithm.HS512, secretKey).compact();

		log.debug("Adding jwt to header");
		res.addHeader(header, tokenPrefix + " " + JWT);
	}

	/**
	 * Checks request object for the presence of a JWT token, and if so, it validates it.
	 * @param request Request object.
	 * @return If the token is present and valid, returns a UserPasswordAuthenticationToken instance. Otherwise, returns null.
	 */
	public Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(header);
		if (token != null) {
			// Check the token
			try {
				Claims userClaims = Jwts.parser()
						.setSigningKey(secretKey)
						.parseClaimsJws(token.replace(tokenPrefix, ""))
						.getBody();
				
				// Create username and authorities 
				String username = userClaims.getSubject();
				String[] authorities = userClaims.get("authorities").toString().replace("[", "").replace("]", "")
						.split(",");
				List<SimpleGrantedAuthority> auths = new ArrayList<SimpleGrantedAuthority>();
				for (int i = 0; i < authorities.length; i++) {
					auths.add(new SimpleGrantedAuthority(authorities[i]));
				}
				
				// If username has been found, return the authentication object.
				if (username != null) {
					return new UsernamePasswordAuthenticationToken(username, auths, Collections.emptyList());
				} else {
					log.debug("Cannae find a username in the given jwt token");
					return null;
				}
				
			} catch (ExpiredJwtException ex) {
				log.debug("Token expired!", ex);
			} catch (Exception ex) {
				log.debug("Error parsing the token!", ex);
			}
		}
		return null;

	}
}
