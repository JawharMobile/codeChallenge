package com.adidas.cloud.security.jwt;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Filters /login requests to create a JWT token from the user credentials.
 * @author nestis
 *
 */
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	/** TokenAuthenticationService instance */
	private TokenAuthenticationService tokenAuthenticationService;

	public JWTLoginFilter(String url, AuthenticationManager authManager,
			TokenAuthenticationService tokenAuthenticationService) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
		this.tokenAuthenticationService = tokenAuthenticationService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException, IOException, ServletException {
		// Get the user credentials
		String user = req.getParameter("user");
		String pass = req.getParameter("password");

		// Try to authenticate the user.
		return getAuthenticationManager()
				.authenticate(new UsernamePasswordAuthenticationToken(user, pass, Collections.emptyList()));
	}

	/**
	 * This method will be called only if the user authenticated was succesful.
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		// Create the JWT token and add it to the header.
		tokenAuthenticationService.addAuthentication(res, auth);
	}
}
