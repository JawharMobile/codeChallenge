package com.adidas.cloud.zuul.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import com.adidas.cloud.zuul.filters.AuthorizationFilter;

/**
 * Zuul Security configuration bean.
 * Configures the zuul server security.
 * @author nestis
 *
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Value("${security.endpoint:http://security/token}")
	public String endpoint;
	
	@Value("${security.secretKey:ChuckBarkley}")
	private String secretKey;

	@Value("${security.tokenPrefix:Bearer}")
	private String tokenPrefix;

	@Value("${security.header:Authorization}")
	private String header;
	
	public void configure(HttpSecurity http) throws Exception {
		http
			// deactivate csrf
	        .csrf().disable()
	        // not create a session
        		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        		.and()
			.authorizeRequests()
			
			// Let anyone login into the app
			.antMatchers("/auth/login").permitAll()
			
			// Let anyone access to hystrix streams and swagger. Just to ease things a wee bit...
			.antMatchers("/hystrix", "/hystrix/**", "/**/hystrix.stream", "/proxy.stream").permitAll()
			.antMatchers("/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/v2/api-docs", "/**/v2/api-docs").permitAll()
			
			// Rest of urls, user has to be authenticated
			.antMatchers("/**").hasAnyRole("USER", "ADMIN");
		
		// Add Authorization filter
		AuthorizationFilter authFilter = new AuthorizationFilter(restTemplate(), endpoint, secretKey, tokenPrefix, header);
		http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	/**
	 * Returns a load balanced instance of RestTemplate.
	 * @return RestTemplate bean.
	 */
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
}
