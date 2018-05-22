package com.adidas.cloud.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Filter that checks the JWT.
 * If the token is valid, it will create a SecurityContext with the user info obtained from the token.
 * @author nestis
 *
 */
public class JWTAuthenticationFilter extends GenericFilterBean {

	/** TokenAuthenticationService bean */
	private TokenAuthenticationService tokenAuthenticationService;
	
	
	@Autowired
	public JWTAuthenticationFilter(TokenAuthenticationService tokenAuthenticationService) {
		this.tokenAuthenticationService = tokenAuthenticationService;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		// Get the user authentication.
		Authentication authentication = tokenAuthenticationService.getAuthentication((HttpServletRequest) request);
		// If the jwt is valid, create the SecurityContext
		if (authentication != null) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		// Continue to next filter, whether the token is valid or not.
		filterChain.doFilter(request, response);
	}
}
