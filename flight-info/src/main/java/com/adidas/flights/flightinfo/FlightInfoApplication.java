package com.adidas.flights.flightinfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * FlightInfo main entry point.
 * @author nestis
 *
 */
@EnableCircuitBreaker
@EnableHystrixDashboard
@SpringBootApplication
public class FlightInfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightInfoApplication.class, args);
	}
}
