package com.adidas.flights.flightinfo.unit.helper.service;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.adidas.flights.flightinfo.services.domain.FlightDto;
import com.adidas.flights.flightinfo.services.domain.RouteDto;
import com.adidas.flights.flightinfo.services.impl.FlightInfoServiceImpl;
import com.adidas.flights.flightinfo.services.impl.FlightInfoWebClient;
import com.adidas.flights.flightinfo.services.impl.ItineraryFacadeImpl;

import reactor.core.publisher.Flux;

public class FlightInfoServiceImplTest {

	private FlightInfoServiceImpl flightInfoTest;
	
	private FlightWebClientMock flightClientMock;
	
	@Before
	public void setup() {

		List<FlightDto> flights = new ArrayList<FlightDto>(0);
		
		FlightDto f1 = new FlightDto();
		f1.setDeparture("Zaragoza");
		f1.setDestination("London");
		f1.setDepartureTime("12:00");
		f1.setArrivalTime("0:40");
		flights.add(f1);
		
		flightClientMock = new FlightWebClientMock();
		flightClientMock.setFlights(flights);
		
		flightInfoTest = new FlightInfoServiceImpl(flightClientMock, new ItineraryFacadeImpl());
	}
	
	@Test
	public void shouldGetShortestRoute() {
		Flux<RouteDto> shortestRoute = flightInfoTest.getShortestItinerary("Zaragoza", "London");
		List<RouteDto> routes = shortestRoute.collectList().block();
		assertThat(routes, hasSize(1));
	}
	
	@Test
	public void shouldGetShortestRouteWithDifferentCapitalLetters() {
		Flux<RouteDto> shortestRoute = flightInfoTest.getShortestItinerary("zAraGozA", "lOnDon");
		List<RouteDto> routes = shortestRoute.collectList().block();
		assertThat(routes, hasSize(1));
	}
	
	@Test
	public void shouldGetQuickestRoute() {
		Flux<RouteDto> shortestRoute = flightInfoTest.getQuickestItinerary("Zaragoza", "London");
		List<RouteDto> routes = shortestRoute.collectList().block();
		assertThat(routes, hasSize(1));
	}
	
	@Test
	public void shouldGetQuickestRouteWithDifferentCapitalLetters() {
		Flux<RouteDto> shortestRoute = flightInfoTest.getShortestItinerary("zAraGozA", "lOnDon");
		List<RouteDto> routes = shortestRoute.collectList().block();
		assertThat(routes, hasSize(1));
	}
	
	@Test
	public void shouldntGetARouteDueToPArseError() {
		List<FlightDto> flights = new ArrayList<FlightDto>(0);
		
		FlightDto f1 = new FlightDto();
		f1.setDeparture("Zaragoza");
		f1.setDestination("London");
		f1.setDepartureTime("12:00");
		f1.setArrivalTime("0x:40");
		flights.add(f1);
		flightClientMock.setFlights(flights);
	
		Flux<RouteDto> shortestRoute = flightInfoTest.getShortestItinerary("zAraGozA", "lOnDon");
		List<RouteDto> routes = shortestRoute.collectList().block();
		assertThat(routes, hasSize(0));
	}
}

class FlightWebClientMock extends FlightInfoWebClient {

	private List<FlightDto> flights; 
	public FlightWebClientMock() {
		super(null);
	}
	
	public void setFlights(List<FlightDto> flights) {
		this.flights = flights;
	}
	
	public Flux<FlightDto> getFligths() {
		return Flux.fromIterable(flights);
	}
}