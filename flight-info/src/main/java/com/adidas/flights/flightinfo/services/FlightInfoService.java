package com.adidas.flights.flightinfo.services;

import com.adidas.flights.flightinfo.services.domain.RouteDto;

import reactor.core.publisher.Flux;

/**
 * Defines the interface for Flight info service.
 * @author nestis
 *
 */
public interface FlightInfoService {

	/**
	 * Method that will return the shortest itinerary for the given origin and destination airports.
	 * @param departure Departure airport
	 * @param destination Destination airport
	 * @return Flux of RouteDto.
	 */
	Flux<RouteDto> getShortestItinerary(String departure, String destination);
	
	/**
	 * Method that will return the quickest itinerary for the given origin and destination airports.
	 * @param departure Departure airport
	 * @param destination Destination airport
	 * @return Flux of RouteDto.
	 */
	Flux<RouteDto> getQuickestItinerary(String departure, String destination);
	
	
	
}
