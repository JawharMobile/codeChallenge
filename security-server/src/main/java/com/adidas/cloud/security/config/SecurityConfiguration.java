package com.adidas.cloud.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.adidas.cloud.security.jwt.JWTAuthenticationFilter;
import com.adidas.cloud.security.jwt.JWTLoginFilter;
import com.adidas.cloud.security.jwt.TokenAuthenticationService;

/**
 * Security configuration.
 * Configures the different filters used to create and check the JWT token.
 * @author nestis
 *
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
	        // disable crsf. don't need it while using the token
	        .csrf().disable()
            // don't create a session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
			.authorizeRequests()
			// let anyone login into the security-server
	        .antMatchers("/login").permitAll()
	        // any other request, has to be authenticated
	        .anyRequest().authenticated();
		
		// Add the security filters
		JWTLoginFilter loginFilter = new JWTLoginFilter("/login", authenticationManager(), tokenAuthenticationService());
		http
			// Filter login requests
			.addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class)
			// Any other requests, check the JWT in header
			.addFilterAfter(new JWTAuthenticationFilter(tokenAuthenticationService()), JWTLoginFilter.class);
	}

	/**
	 * Creates a in-memory user management.
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("admin").password("{noop}password").roles("ADMIN", "USER")
			.and()
			.withUser("user").password("{noop}user").roles("USER");
	}
	
	/**
	 * Creates and instance of TokenAuthenticationService.
	 * @return
	 */
	@Bean
	public TokenAuthenticationService tokenAuthenticationService() {
		return new TokenAuthenticationService();
	}
}
