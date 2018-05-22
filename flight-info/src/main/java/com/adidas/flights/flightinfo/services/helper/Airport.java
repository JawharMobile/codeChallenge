package com.adidas.flights.flightinfo.services.helper;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NonNull;

/**
 * Class that defines a Airport.
 * It will be used by the route finding logic
 * @author nestis
 *
 */
@Data
public class Airport {

	/** Airport name */
	@NonNull
	private String departure;

	/** List of possible destinations. Using a list let us add the same destination twice */
	private List<Destination> destinations;
	
	/**
	 * Class constructor
	 * @param departure Airport departure city.
	 */
	public Airport(String departure) {
		this.departure = departure;
		this.destinations = new ArrayList<Destination>(0);
	}
	
}
