package com.adidas.flights.flightinfo.services.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.adidas.flights.flightinfo.services.ItineraryFacade;
import com.adidas.flights.flightinfo.services.domain.FlightDto;
import com.adidas.flights.flightinfo.services.domain.RouteDto;
import com.adidas.flights.flightinfo.services.helper.Airport;
import com.adidas.flights.flightinfo.services.helper.Destination;
import com.adidas.flights.flightinfo.services.helper.RouteFinderTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ItineraryFacadeImpl implements ItineraryFacade {
	
	public List<RouteDto> getItinerary(RouteFinderTemplate routeStrategy, List<FlightDto> flights) {
		log.debug("Call to ItineraryFacade with {} flights", flights.size());
		
		Map<String, Airport> airports = buildFlightGraph(flights);
		log.debug("Return strategy info");
		return routeStrategy.getInfo(airports);
	}
	
	/**
	 * Gets the graph for the given list of flights.
	 * @param flights list of flights
	 * @return HashMap containing the graph of flights. The map key will the departure airport (for fast access)
	 * and the value will be an Airport instance.
	 */
	private Map<String, Airport> buildFlightGraph(List<FlightDto> flights) {
		
		Map<String, Airport> airports = new HashMap<String, Airport>();
		
		flights.forEach(fl -> {
			// Check if the origin airport is already in the map
			Airport airport = airports.get(fl.getDeparture().toLowerCase());
			
			// If not, add it
			if (airport == null) {
				airport = new Airport(fl.getDeparture().toLowerCase());
				airports.put(fl.getDeparture().toLowerCase(), airport);
			}
			
			// Add the destination to the list of destinations of the airport
			try {
				Airport destination = airports.get(fl.getDestination().toLowerCase());
				if (destination == null) {
					destination = new Airport(fl.getDestination().toLowerCase());
					airports.put(fl.getDestination().toLowerCase(), destination);
				}
				
				Long time = getTime(fl.getDepartureTime(), fl.getArrivalTime());
				airport.getDestinations().add(new Destination(destination, time));
			} catch (ParseException e) {
				log.error("Error parsing departure or arrival time. Flight will no be added", e);
			}
		});
		
		return airports;
	}
	
	/**
	 * Will return the flight time between two airports.
	 * To make things easy, we don't take into account the different timezones.
	 * Our flights schedule will only have one timezone.
	 * @param departure Departure time.
	 * @param arrival Arrival time.
	 * @return Time flight expressed in millis.
	 * @throws ParseException In case something goes south when parsing the times.
	 */
	private Long getTime(String departure, String arrival) throws ParseException {
		DateFormat sdf = new SimpleDateFormat("HH:mm");
		Date depTime = sdf.parse(departure);
		Date arrTime = sdf.parse(arrival);
		
		// If the arrival time is lesser than the departure time, means we're arrival the next day, so add a whole day to the arrTime variable.
		// to make things easy, we don't take into account the different timezones.
		// Our flights schedule will only have one timezone.
		if (arrTime.before(depTime)) {
			arrTime.setTime(arrTime.getTime() + 86400000);
		}
		
		return arrTime.getTime() - depTime.getTime();
	}
}
