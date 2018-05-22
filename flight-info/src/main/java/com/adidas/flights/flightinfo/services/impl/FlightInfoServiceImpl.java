package com.adidas.flights.flightinfo.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adidas.flights.flightinfo.exceptions.FlightInfoException;
import com.adidas.flights.flightinfo.services.FlightInfoService;
import com.adidas.flights.flightinfo.services.ItineraryFacade;
import com.adidas.flights.flightinfo.services.domain.FlightDto;
import com.adidas.flights.flightinfo.services.domain.RouteDto;
import com.adidas.flights.flightinfo.services.helper.QuickestRoute;
import com.adidas.flights.flightinfo.services.helper.RouteFinderTemplate;
import com.adidas.flights.flightinfo.services.helper.ShortestRoute;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * Implementation of FlightInfoService.
 * @author nestis
 */
@Slf4j
@Service
public class FlightInfoServiceImpl implements FlightInfoService {
	
	/** WebClient used to call FlightData endpoint */
	private FlightInfoWebClient flightWebClient;
	
	private ItineraryFacade itineraryFacade;
	
	@Autowired
	public FlightInfoServiceImpl(FlightInfoWebClient flightWebClient, ItineraryFacade itineraryFacade) {
		this.flightWebClient = flightWebClient;
		this.itineraryFacade = itineraryFacade;
	}

	public Flux<RouteDto> getShortestItinerary(String departure, String destination) {
		log.debug("Call to getShortestItinerary({}, {})", departure, destination);
		return getItinerary(new ShortestRoute(departure, destination));
	}

	public Flux<RouteDto> getQuickestItinerary(String departure, String destination) {
		log.debug("Call to getQuickestItinerary({}, {})", departure, destination);
		return getItinerary(new QuickestRoute(departure, destination));
	}
	
	private Flux<RouteDto> getItinerary(RouteFinderTemplate routeStrategy) {
		// Call FlightData service
		return flightWebClient
				.getFligths()
				.collectList()
				// Get the list and call to ItineraryFacade to obtain the route/s
				.flatMapMany(flights -> Flux.fromIterable(itineraryFacade.getItinerary(routeStrategy, (List<FlightDto>) flights)))
				// If anything goes wrong, return an exception
				.onErrorMap(exception -> {
					log.error("Error processing the flights route", exception);
					throw new FlightInfoException("Error processing the flights route", exception);
				});
	}

}
