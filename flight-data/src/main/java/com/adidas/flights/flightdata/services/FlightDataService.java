package com.adidas.flights.flightdata.services;

import com.adidas.flights.flightdata.entities.FlightEntity;

import reactor.core.publisher.Flux;

/**
 * Interface that defines the Flight Data service
 * @author nestis
 *
 */
public interface FlightDataService {

	/**
	 * Returns all flights on db
	 * @return Flux of FlightEntity
	 */
	Flux<FlightEntity> getFlights();
	
}
