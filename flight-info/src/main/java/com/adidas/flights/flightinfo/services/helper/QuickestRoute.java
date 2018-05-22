package com.adidas.flights.flightinfo.services.helper;

import java.util.ArrayList;
import java.util.List;

import com.adidas.flights.flightinfo.services.domain.RouteDto;

/**
 * Implements the logic to obtain the quickest route between two airports.
 * @author nestis
 *
 */
public class QuickestRoute extends RouteFinderTemplate {

	/**
	 * Class constructor
	 * @param departure Departure airport
	 * @param destination Destination airport
	 */
	public QuickestRoute(String departure, String destination) {
		super(departure, destination);
	}

	/**
	 * Returns a list of RouteDto containing the quickest routes between the origin and destination airports.
	 */
	@Override
	public List<RouteDto> getResult(List<RouteDto> routesFound) {
		Long quickestTime = Long.MAX_VALUE;
		List<RouteDto> quickestRoutes = new ArrayList<RouteDto>();
		// Iterate over all the routes found
		for(RouteDto route: routesFound) {
			// If the current route time is lesser than the quickestTime
			// clear the list, there could be routes added before, and add the current route
			if  (route.getTime() < quickestTime) {
				if (!quickestRoutes.isEmpty()) {
					quickestRoutes.clear();
				}
				quickestRoutes.add(route);
				quickestTime = route.getTime();
			// If the current route time is equal to the quickest time, add it to the list
			} else if (route.getTime().intValue() == quickestTime) {
				quickestRoutes.add(route);
			}
		}
		return quickestRoutes;
	}
}

