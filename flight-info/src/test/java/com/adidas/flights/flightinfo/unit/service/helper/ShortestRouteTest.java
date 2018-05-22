package com.adidas.flights.flightinfo.unit.service.helper;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.adidas.flights.flightinfo.services.domain.RouteDto;
import com.adidas.flights.flightinfo.services.helper.Airport;
import com.adidas.flights.flightinfo.services.helper.Destination;
import com.adidas.flights.flightinfo.services.helper.ShortestRoute;

public class ShortestRouteTest {

	private ShortestRoute shortestRoute;

	@Test
	public void shouldReturnOneRoute() {
		Map<String, Airport> flights = new HashMap<String, Airport>();
		Airport zgz = new Airport("zaragoza");
		flights.put("zaragoza", zgz);
		Airport london = new Airport("london");
		flights.put("london", london);
		Airport frankfurt = new Airport("frankfurt");
		flights.put("frankfurt", frankfurt);
		Airport amsterdam = new Airport("amsterdam");
		flights.put("amsterdam", amsterdam);
		Airport glasgow = new Airport("glasgow");
		flights.put("glasgow", glasgow);
		Airport leicester = new Airport("leicester");
		flights.put("leicester", leicester);
		
		zgz.getDestinations().add(new Destination(london, 21000L));
		zgz.getDestinations().add(new Destination(frankfurt, 15000L));
		
		london.getDestinations().add(new Destination(zgz, 20000L));
		london.getDestinations().add(new Destination(glasgow, 10000L));
		london.getDestinations().add(new Destination(leicester, 9000L));
		london.getDestinations().add(new Destination(amsterdam, 15000L));
		london.getDestinations().add(new Destination(frankfurt, 15000L));
		
		frankfurt.getDestinations().add(new Destination(zgz, 20000L));
		frankfurt.getDestinations().add(new Destination(leicester, 21000L));
		frankfurt.getDestinations().add(new Destination(london, 20000L));
		
		shortestRoute = new ShortestRoute("Zaragoza", "Glasgow");
		List<RouteDto> routes = shortestRoute.getInfo(flights);
		
		assertThat(routes,hasSize(1));
		RouteDto route = routes.get(0);
		assertThat(route.getRoute(), hasSize(3));
		assertThat(route.getRoute(), contains("zaragoza", "london", "glasgow"));
	}
	

	@Test
	public void shouldReturnTwoRoutes() {
		Map<String, Airport> flights = new HashMap<String, Airport>();
		Airport zgz = new Airport("zaragoza");
		flights.put("zaragoza", zgz);
		Airport london = new Airport("london");
		flights.put("london", london);
		Airport frankfurt = new Airport("frankfurt");
		flights.put("frankfurt", frankfurt);
		Airport glasgow = new Airport("glasgow");
		flights.put("glasgow", glasgow);
		
		zgz.getDestinations().add(new Destination(london, 19000L));
		zgz.getDestinations().add(new Destination(frankfurt, 21000L));
		
		london.getDestinations().add(new Destination(zgz, 20000L));
		london.getDestinations().add(new Destination(glasgow, 10000L));
		
		frankfurt.getDestinations().add(new Destination(zgz, 20000L));
		frankfurt.getDestinations().add(new Destination(glasgow, 10000L));
		
		shortestRoute = new ShortestRoute("Zaragoza", "Glasgow");
		List<RouteDto> routes = shortestRoute.getInfo(flights);
		
		assertThat(routes,hasSize(2));
		RouteDto route = routes.get(0);
		assertTrue(route.getTime().equals(29000L));
		assertThat(route.getRoute(), hasSize(3));
		assertThat(route.getRoute(), contains("zaragoza", "london", "glasgow"));
		
		route = routes.get(1);
		assertTrue(route.getTime().equals(31000L));
		assertThat(route.getRoute(), hasSize(3));
		assertThat(route.getRoute(), contains("zaragoza", "frankfurt", "glasgow"));
	}
	
	@Test
	public void shouldNotFindARoute() {
		Map<String, Airport> flights = new HashMap<String, Airport>();
		Airport zgz = new Airport("zaragoza");
		flights.put("zaragoza", zgz);
		Airport london = new Airport("london");
		flights.put("london", london);
		Airport frankfurt = new Airport("frankfurt");
		flights.put("frankfurt", frankfurt);
		Airport glasgow = new Airport("glasgow");
		flights.put("glasgow", glasgow);
		
		zgz.getDestinations().add(new Destination(london, 21000L));
		zgz.getDestinations().add(new Destination(frankfurt, 21000L));
		
		london.getDestinations().add(new Destination(zgz, 20000L));
		
		frankfurt.getDestinations().add(new Destination(zgz, 20000L));
		
		shortestRoute = new ShortestRoute("Zaragoza", "Glasgow");
		List<RouteDto> routes = shortestRoute.getInfo(flights);
		
		assertThat(routes,hasSize(0));
	}
}
