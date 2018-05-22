package com.adidas.flights.flightinfo.exceptions;

import lombok.Data;

/**
 * FlightInfoException class.
 * All the exceptions within the application should be embedded in this RuntimeException, following Spring practices.
 * @author nestis
 *
 */
@Data
public class FlightInfoException extends RuntimeException {

	/** Generated UID */
	private static final long serialVersionUID = -2844080813471994465L;

	/** Source exception */
	private Throwable exception;
	
	/** Custom message */
	private String message;
	
	/**
	 * Class constructor.
	 * @param message Exception message.
	 */
	public FlightInfoException(String message) {
		this.message = message;
	}
	
	/**
	 * Class constructor.
	 * @param message Exception message.
	 * @param ex Exception raised.
	 */
	public FlightInfoException(String message, Throwable ex) {
		this.message = message;
		this.exception = ex;
	}
}
