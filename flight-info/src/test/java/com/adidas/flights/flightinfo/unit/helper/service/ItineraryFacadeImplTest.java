package com.adidas.flights.flightinfo.unit.helper.service;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.adidas.flights.flightinfo.services.domain.FlightDto;
import com.adidas.flights.flightinfo.services.domain.RouteDto;
import com.adidas.flights.flightinfo.services.helper.QuickestRoute;
import com.adidas.flights.flightinfo.services.helper.ShortestRoute;
import com.adidas.flights.flightinfo.services.impl.ItineraryFacadeImpl;

public class ItineraryFacadeImplTest {

	private ItineraryFacadeImpl itineraryFacade;
	
	@Before
	public void setup() {
		this.itineraryFacade = new ItineraryFacadeImpl();
	}
	
	@Test
	public void shouldGetAShortestRoute() {

		List<FlightDto> flights = new ArrayList<FlightDto>(0);
		
		FlightDto f1 = new FlightDto();
		f1.setDeparture("Zaragoza");
		f1.setDestination("London");
		f1.setDepartureTime("12:00");
		f1.setArrivalTime("13:00");
		flights.add(f1);
		
		FlightDto f2 = new FlightDto();
		f2.setDeparture("London");
		f2.setDestination("Glasgow");
		f2.setDepartureTime("17:00");
		f2.setArrivalTime("17:50");
		flights.add(f2);
		
		FlightDto f3 = new FlightDto();
		f3.setDeparture("Zaragoza");
		f3.setDestination("Glasgow");
		f3.setDepartureTime("17:00");
		f3.setArrivalTime("18:40");
		flights.add(f3);
		
		List<RouteDto> routes = itineraryFacade.getItinerary(new ShortestRoute("Zaragoza", "Glasgow"), flights);
		assertThat(routes, hasSize(1));
	}
	
	@Test
	public void shouldGetAQuickestRoute() {

		List<FlightDto> flights = new ArrayList<FlightDto>(0);
		
		FlightDto f1 = new FlightDto();
		f1.setDeparture("Zaragoza");
		f1.setDestination("London");
		f1.setDepartureTime("12:00");
		f1.setArrivalTime("13:00");
		flights.add(f1);
		
		FlightDto f2 = new FlightDto();
		f2.setDeparture("London");
		f2.setDestination("Glasgow");
		f2.setDepartureTime("17:00");
		f2.setArrivalTime("17:50");
		flights.add(f2);
		
		FlightDto f3 = new FlightDto();
		f3.setDeparture("Zaragoza");
		f3.setDestination("Glasgow");
		f3.setDepartureTime("17:00");
		f3.setArrivalTime("18:40");
		flights.add(f3);
		
		List<RouteDto> routes = itineraryFacade.getItinerary(new QuickestRoute("Zaragoza", "Glasgow"), flights);
		assertThat(routes, hasSize(1));
	}
	
	@Test
	public void shouldGetTwoShortestRoutes() {

		List<FlightDto> flights = new ArrayList<FlightDto>(0);
		
		FlightDto f1 = new FlightDto();
		f1.setDeparture("Zaragoza");
		f1.setDestination("London");
		f1.setDepartureTime("12:00");
		f1.setArrivalTime("13:00");
		flights.add(f1);
		
		FlightDto f2 = new FlightDto();
		f2.setDeparture("London");
		f2.setDestination("Glasgow");
		f2.setDepartureTime("17:00");
		f2.setArrivalTime("17:40");
		flights.add(f2);
		
		FlightDto f3 = new FlightDto();
		f3.setDeparture("Zaragoza");
		f3.setDestination("Glasgow");
		f3.setDepartureTime("17:00");
		f3.setArrivalTime("18:40");
		flights.add(f3);
		
		List<RouteDto> routes = itineraryFacade.getItinerary(new QuickestRoute("Zaragoza", "Glasgow"), flights);
		assertThat(routes, hasSize(2));
	}
	
	@Test
	public void shouldGetOneShortestRoutesAndIgnoreOneFlightDueToFormatException() {

		List<FlightDto> flights = new ArrayList<FlightDto>(0);
		
		FlightDto f1 = new FlightDto();
		f1.setDeparture("Zaragoza");
		f1.setDestination("London");
		f1.setDepartureTime("12:00");
		f1.setArrivalTime("13:00");
		flights.add(f1);
		
		FlightDto f2 = new FlightDto();
		f2.setDeparture("London");
		f2.setDestination("Glasgow");
		f2.setDepartureTime("17:00");
		f2.setArrivalTime("17:40");
		flights.add(f2);
		
		FlightDto f3 = new FlightDto();
		f3.setDeparture("Zaragoza");
		f3.setDestination("Glasgow");
		f3.setDepartureTime("17:00");
		f3.setArrivalTime("F8:40");
		flights.add(f3);
		
		List<RouteDto> routes = itineraryFacade.getItinerary(new QuickestRoute("Zaragoza", "Glasgow"), flights);
		assertThat(routes, hasSize(1));
		assertThat(routes.get(0).getRoute(), contains("zaragoza", "london", "glasgow"));
		
	}
}
