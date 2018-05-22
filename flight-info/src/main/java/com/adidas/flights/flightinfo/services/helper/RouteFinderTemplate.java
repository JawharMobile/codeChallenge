package com.adidas.flights.flightinfo.services.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.adidas.flights.flightinfo.services.domain.RouteDto;

/**
 * Template class that finds all the routes between two airports and delegates to its subclasses
 * the logic to obtain what kind of route are they looking for, Shortest, Quickest...
 * @author nestis
 *
 */
public abstract class RouteFinderTemplate {

	/** Origin airport */
	protected String origin;
	
	/** Destination airport */
	protected String destination;
	
	/** List that will contain all the routes found */
	protected List<RouteDto> routesFound;
	
	/**
	 * Class constructor.
	 * @param origin Origin airport.
	 * @param dest Destination airport.
	 */
	public RouteFinderTemplate(String origin, String destination) {
		this.origin = origin;
		this.destination = destination;
	}

	/**
	 * Returns the list of routes that matches the selected logic.
	 * This method must be implemented by RouteFinderTemplate subclasses.
	 * @param routesFound List of Route
	 * @return List of RouteDto containing the Route selected by the subclass.
	 */
	protected abstract List<RouteDto> getResult(List<RouteDto> routesFound);

	/**
	 * Gets the route or routes between the departure and destination airport, using the flights graph passed as an argument.
	 * @param flights Graph of Airports and its destinations.
	 * @return List of RouteDto that matches the business logic.
	 */
	public List<RouteDto> getInfo(Map<String, Airport> flights) {
		// Create the cheapest route object
		routesFound = new ArrayList<RouteDto>();
		getRoutes(flights);
		return getResult(routesFound);
	}
	
	/**
	 * Get the routes between origin and destination for the given flights.
	 * @param flights Maps containing destination name and airport object.
	 */
	protected void getRoutes(Map<String, Airport> flights) {
		// Get the origin airport
		Airport originAirport = flights.get(origin.toLowerCase());
		
		if (originAirport != null) {
			// Iterate over its destinations to find the cheapest route
			originAirport.getDestinations().forEach(d -> {
				// Set the origin as the first item in Route object
				RouteDto route = new RouteDto(origin.toLowerCase());
				route.setTime(route.getTime() + d.getTime());
				
				// If the this step is the destination, add it to the array
				route.getRoute().add(d.getDestination().getDeparture());
				if (d.getDestination().getDeparture().toLowerCase().equals(destination.toLowerCase())) {
	 				routesFound.add(new RouteDto(route.getTime(), route.getRoute()));
				}
				// Iterate over its children to find another possible route
				iterateOverChild(d, route);
			});
			
		}
	}
	
	/**
	 * Iterate over all the the destinations of a destination.
	 * We iterate over all of them because there can be more than one way to get to the destination.
	 * @param destinationAirport Destination to iterate over
	 * @param route Route taken to get here.
	 * @return Boolean. If the destination is found or not.
	 */
 	protected void iterateOverChild(Destination destinationAirport, RouteDto route) {
 		// Iterate over all the destinations
 		List<Destination> destinations = destinationAirport.getDestination().getDestinations();
 		
 		// Iterate over the destinations
 		for(Destination d : destinations) {
 			
 			// If the current destination is the one we are looking for...
 			if (d.getDestination().getDeparture().equalsIgnoreCase(destination)) {
 				
 				// Add its value and save the current route into routeFound array
				route.setTime(route.getTime() + d.getTime());
 				route.getRoute().add(d.getDestination().getDeparture().toLowerCase());
 				routesFound.add(new RouteDto(route.getTime(), route.getRoute()));
 				
 				// Once the route has been added to the array, we can remove this step from the route object.
 				// because the iteration will continue when we get back to the previous caller.
 				// The flow will return to the previous call so we have to delete this iteration.
 				// The parent node will continue the execution flow with the next child (if any)
				route.setTime(route.getTime() - d.getTime());
				route.getRoute().remove(route.getRoute().size() - 1);
					
			// If the current route is not the destination and it hasn't been previously visited (we wouldn't want to fly to the same places over and over again...)
 			} else if (!route.getRoute().contains(d.getDestination().getDeparture().toLowerCase())){
 				// Add its time and the location and iterate over its children destinations
				route.setTime(route.getTime() + d.getTime());
 				route.getRoute().add(d.getDestination().getDeparture().toLowerCase());
 				iterateOverChild(d, route);
 				
 				// Once we have iterate over the children, we can delete this route.
 				// Same reasoning as above
				route.setTime(route.getTime() - d.getTime());
				route.getRoute().remove(route.getRoute().size() - 1);
 			}
 		}
 	}
}
