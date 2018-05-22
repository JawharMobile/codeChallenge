package com.adidas.flights.flightinfo.services;

import java.util.List;

import com.adidas.flights.flightinfo.services.domain.FlightDto;
import com.adidas.flights.flightinfo.services.domain.RouteDto;
import com.adidas.flights.flightinfo.services.helper.RouteFinderTemplate;

/**
 * Class that provides a facade to obtain the itinerary between two airports.
 * This class uses the a variation of the Strategy pattern, the Strategy is passed
 * as an argument, rather that create a class property and modify it dynamically.
 * The reason behind this, is that we are on a multi threaded environment. We can set the strategy
 * property to X, but if there is another request just after the first has been received, the first one
 * will use the strategy selected by the second one.
 * @author nestis
 *
 */
public interface ItineraryFacade {
	
	/**
	 * Method that gets the itinerary between two airports using the given strategy.
	 * @param routeStrategy Route strategy to be used. Shortest, Quickest...
	 * @param flights List of flights.
	 * @return List of RouteDto containing the routes found
	 */
	List<RouteDto> getItinerary(RouteFinderTemplate routeStrategy, List<FlightDto> flights);
	
}
