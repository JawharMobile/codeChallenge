package com.adidas.flights.flightinfo.services.domain;

import lombok.Data;

/**
 * Class that defines a Flight.
 * @author nestis
 *
 */
@Data
public class FlightDto {
	
	/** Departure airport **/
	private String departure;
	
	/** Destination airport **/
	private String destination;
	
	/** Departure time **/
	private String departureTime;
	
	/** Arrival time **/
	private String arrivalTime;
}
