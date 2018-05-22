package com.adidas.flights.flightinfo.services.helper;

import java.util.ArrayList;
import java.util.List;

import com.adidas.flights.flightinfo.services.domain.RouteDto;

/**
 * Implements the logic to obtain the shortest route between two airports.
 * @author nestis
 *
 */
public class ShortestRoute extends RouteFinderTemplate {

	/**
	 * Class constructor
	 * @param departure Departure airport
	 * @param destination Destination airport
	 */
	public ShortestRoute(String departure, String destination) {
		super(departure, destination);
	}

	/**
	 * Return a list of RouteDto containing the routes between the origin and the destination airport
	 * with the minimum number of stopovers.
	 */
	@Override
	public List<RouteDto> getResult(List<RouteDto> routesFound) {
		Integer shortest = Integer.MAX_VALUE;
		List<RouteDto> shortestRoutes = new ArrayList<RouteDto>();
		// Iterate over all the routes found
		for(RouteDto route: routesFound) {
			// If the current route number of stop overs is lesser than the shortest number
			// clear the list, there could be routes added before, and add the current route
			if  (route.getRoute().size() < shortest) {
				if (!shortestRoutes.isEmpty()) {
					shortestRoutes.clear();
				}
				shortestRoutes.add(route);
				shortest = route.getRoute().size();
			// If the current route stop overs is equal to the shortest number time, add it to the list
			} else if (route.getRoute().size() == shortest) {
				shortestRoutes.add(route);
			}
		}
		return shortestRoutes;
	}
}
