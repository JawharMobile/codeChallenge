package com.adidas.cloud.zuul.filters;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

/**
 * Zuul Authorization Filter. 
 * This filter checks if the user is authenticated against the security-server.
 * If so, it will create a SecurityContext with the user info and its authorities.
 * To create this SecurityContext, it must decode the JWT so the security zuul configuration must be exactly the same
 * as the security server config.
 * @author nestis
 *
 */
@Slf4j
public class AuthorizationFilter extends GenericFilterBean {

	/** JWT secret key **/
	private String secretKey;

	/** JWT token prefix */
	private String tokenPrefix;

	/** JWT request header name */
	private String header;

	/** Balanced RestTemplate */
	private RestTemplate restClient;

	/** Security server endpoint */
	private String securityServer;

	/**
	 * Filter constructor.
	 * @param restClient RestTemplate. Ideally, it should be a balanced instance.
	 * @param securityServer Security server endpoint.
	 * @param secretKey JWT secret key.
	 * @param tokenPrefix JWT token prefix.
	 * @param header JWT request header name.
	 */
	public AuthorizationFilter(RestTemplate restClient, String securityServer, String secretKey, String tokenPrefix,
			String header) {
		this.secretKey = secretKey;
		this.tokenPrefix = tokenPrefix;
		this.header = header;
		this.restClient = restClient;
		this.securityServer = securityServer;
	}

	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		// Check the user authentication...
		if (isUserAuthenticated(req)) {
			setAuthentication(req);
		}
		chain.doFilter(request, response);
	}

	/**
	 * Checks if the user is correctly authenticated by dispatching a request to security-server. 
	 * @param request HttpServletRequest object.
	 * @return Boolean. True is the user is authenticated, false otherwise.
	 */
	private boolean isUserAuthenticated(HttpServletRequest request) {
		// Gets the authorization header from the request.
		String authHeader = request.getHeader(header);

		if (authHeader != null) {
			// Create the request to security-server using the authorization header.
			HttpHeaders headers = new HttpHeaders();
			headers.set(header, authHeader);
			HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

			try {
				// Dispatch request. If the request is 2xx the user is authenticated.
				// Any other case (exception, status != 200) the user is NOT authenticated.
				ResponseEntity<String> securityResponse = restClient.exchange(new URI(securityServer), HttpMethod.GET, entity, String.class);
				if (securityResponse.getStatusCode().is2xxSuccessful()) {
					log.debug("User is authenticated!");
					return true;
				} else {
					return false;
				}
			} catch (RestClientException e) {
				log.debug("RestClientException thrown!", e);
			} catch (URISyntaxException e) {
				log.debug("Error parsing the URL {}", securityServer, e);
			} catch (IllegalStateException ex) {
				log.debug("IllegalStateException thrown!", ex);
			}
		}
		return false;
	}

	/**
	 * Decodes the JWT token and creates a SecurityContext with the user info obtained from the token.
	 * @param request HttpServletRequest object.
	 */
	private void setAuthentication(HttpServletRequest request) {
		String token = request.getHeader(header);
		try {
			// Parse the token and get Claims object.
			Claims userClaims = Jwts.parser()
					.setSigningKey(secretKey)
					.parseClaimsJws(token.replace(tokenPrefix, ""))
					.getBody();
	
			// Get the username and its authorities
			String username = userClaims.getSubject();
			String[] authorities = userClaims.get("authorities").toString().replace("[", "").replace("]", "").split(",");
			List<SimpleGrantedAuthority> auths = new ArrayList<SimpleGrantedAuthority>();
			for (int i = 0; i < authorities.length; i++) {
				auths.add(new SimpleGrantedAuthority(authorities[i]));
			}
	
			// If there is an user
			if (username != null) {
				Authentication auth = new UsernamePasswordAuthenticationToken(username, "", auths);
				SecurityContextHolder.getContext().setAuthentication(auth);
				log.debug("Security context created for username {}", username);
			}
		} catch (Exception ex) {
			// Is there is an exception while decoding the token, just don't create the security context and
			// let the request go through the next filter. Maybe the next one can create a valid security context.
			log.debug("Exception thrown while setting user authentication!", ex);
		}
	}
}
