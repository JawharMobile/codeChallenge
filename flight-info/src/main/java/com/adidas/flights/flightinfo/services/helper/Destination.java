package com.adidas.flights.flightinfo.services.helper;

import lombok.Data;
import lombok.NonNull;

/**
 * Class that defines a Destination.
 * @author nestis
 *
 */
@Data
public class Destination {

	/** Destination airport name */
	@NonNull
	private Airport destination;
	
	/** Flight time from the containing Airport instance to this destination*/
	@NonNull
	private Long time;
	
}
