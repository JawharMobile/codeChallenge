package com.adidas.flights.flightinfo.services.impl;

import java.net.URI;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.adidas.flights.flightinfo.exceptions.FlightInfoException;
import com.adidas.flights.flightinfo.services.domain.FlightDto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * Service that calls FlightData endpoint to obtain the list of flights.
 * @author nestis
 *
 */
@Slf4j
@Service
public class FlightInfoWebClient {
	
	/** FlightData endpoint URL */
	@Value("${app.endpoints.flightsData:http://flightData/flightsData}")
	private String FLIGHTS_DATA_URL;

	private WebClient.Builder webClient;
	
	/**
	 * Class constructor.
	 * @param webClient WebClient.Builder bean.
	 */
	@Autowired
	public FlightInfoWebClient(WebClient.Builder webClient) {
		this.webClient = webClient;
	};
	
	/**
	 * Method that dispatches a request to FlightData endpoint and return its response.
	 * Because of Hystrix, if the request fails, a default response will be returned.
	 * @return
	 */
	@HystrixCommand(fallbackMethod = "defaultFlights")
	public Flux<FlightDto> getFligths() {
		try {
			return webClient
					.build()
					.get()
					.uri(new URI(FLIGHTS_DATA_URL))
					.retrieve()
					.bodyToFlux(FlightDto.class);
		
		} catch (Exception e) {
			log.error("Error trying to parse URL", e);
			throw new FlightInfoException("Error parsing WebClient");
		}
	}
	
	/**
	 * Fallback method for getFligths()
	 * @return Empty Flux
	 */
	@SuppressWarnings("unused")
	private Flux<FlightDto> defaultFlights() {
		log.debug("Fallback method call!");
		return Flux.fromIterable(Collections.emptyList());
	}
}
