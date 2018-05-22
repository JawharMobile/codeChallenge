package com.adidas.flights.flightinfo.exceptions;

import lombok.Data;
import lombok.NonNull;

/**
 * FlightInfoRestException.
 * This intent of this class is to define a unique exception to be thrown by the app to the exterior.
 * @author nestis
 *
 */
@Data
public class FlightInfoRestException {

	/**
	 * Timestamp of the exception.
	 */
	@NonNull
	private Long timestamp;
	
	/**
	 * Exception message
	 */
	@NonNull
	private String message;
	
}
