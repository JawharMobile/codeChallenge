package com.adidas.flights.flightinfo.configuration;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * App Configuration bean.
 * Configures common beans used by the application.
 * @author nestis
 *
 */
@Configuration
public class AppConfiguration {

	/**
	 * Instantiates a LoadBalanced WebClient.Builder.
	 * This WebClient will use Ribbon if Eureka is configured.
	 * @return WebClient.Builder
	 */
	@LoadBalanced
	@Bean
	public WebClient.Builder webClient() {
		return WebClient.builder();
	}
}
