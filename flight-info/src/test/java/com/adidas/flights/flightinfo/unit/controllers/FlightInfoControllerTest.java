package com.adidas.flights.flightinfo.unit.controllers;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.adidas.flights.flightinfo.controllers.domain.FlightInfoDtoIn;
import com.adidas.flights.flightinfo.exceptions.FlightInfoException;
import com.adidas.flights.flightinfo.services.FlightInfoService;
import com.adidas.flights.flightinfo.services.domain.RouteDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
		"eureka.client.enabled=false"
})
public class FlightInfoControllerTest {

	@MockBean
	private FlightInfoService flightInfoService;
	
	@Autowired
	private WebTestClient webTestClient;
	
	@Test
	public void shouldGetQuickestRoute() throws Exception {

		List<RouteDto> routesDto = new ArrayList<RouteDto>();
		
		List<String> routes = new ArrayList<>();
		routes.add("zaragoza");
		routes.add("london");
		routes.add("glasgow");
		RouteDto glasgow = new RouteDto(21000L, routes);
		routesDto.add(glasgow);
		given(flightInfoService.getQuickestItinerary("zaragoza", "glasgow")).willReturn(Flux.fromIterable(routesDto));
		
		FlightInfoDtoIn dtoIn = new FlightInfoDtoIn();
		dtoIn.setDeparture("zaragoza");
		dtoIn.setDestination("glasgow");
		
		webTestClient.post()
			.uri("/quickest")
	        .body(Mono.just(dtoIn), FlightInfoDtoIn.class)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
			.expectBody()
			.jsonPath("$[0].time").isEqualTo("21000")
			.jsonPath("$[0].route").isArray();
	}
	
	@Test
	public void shouldGetShortestRoute() throws Exception {

		List<RouteDto> routesDto = new ArrayList<RouteDto>();
		
		List<String> routes = new ArrayList<>();
		routes.add("zaragoza");
		routes.add("london");
		routes.add("glasgow");
		RouteDto glasgow = new RouteDto(21000L, routes);
		routesDto.add(glasgow);
		given(flightInfoService.getShortestItinerary("zaragoza", "glasgow")).willReturn(Flux.fromIterable(routesDto));
		
		FlightInfoDtoIn dtoIn = new FlightInfoDtoIn();
		dtoIn.setDeparture("zaragoza");
		dtoIn.setDestination("glasgow");
		
		webTestClient.post()
			.uri("/shortest")
	        .body(Mono.just(dtoIn), FlightInfoDtoIn.class)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
			.expectBody()
			.jsonPath("$[0].time").isEqualTo("21000")
			.jsonPath("$[0].route").isArray();
	}
	
	@Test
	public void shouldGetAnExceptionMessage() throws Exception {

		List<RouteDto> routesDto = new ArrayList<RouteDto>();
		
		List<String> routes = new ArrayList<>();
		routes.add("zaragoza");
		routes.add("london");
		routes.add("glasgow");
		RouteDto glasgow = new RouteDto(21000L, routes);
		routesDto.add(glasgow);
		given(flightInfoService.getShortestItinerary("zaragoza", "glasgow"))
			.willThrow(new FlightInfoException("test exception"));
		
		FlightInfoDtoIn dtoIn = new FlightInfoDtoIn();
		dtoIn.setDeparture("zaragoza");
		dtoIn.setDestination("glasgow");
		
		webTestClient.post()
			.uri("/shortest")
	        .body(Mono.just(dtoIn), FlightInfoDtoIn.class)
			.exchange()
			.expectStatus().is5xxServerError()
			.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
			.expectBody()
			.jsonPath("$.message").exists();
	}
	
	@Test
	public void shouldGetAnRuntimeExceptionMessage() throws Exception {

		List<RouteDto> routesDto = new ArrayList<RouteDto>();
		
		List<String> routes = new ArrayList<>();
		routes.add("zaragoza");
		routes.add("london");
		routes.add("glasgow");
		RouteDto glasgow = new RouteDto(21000L, routes);
		routesDto.add(glasgow);
		given(flightInfoService.getShortestItinerary("zaragoza", "glasgow"))
			.willThrow(new RuntimeException());
		
		FlightInfoDtoIn dtoIn = new FlightInfoDtoIn();
		dtoIn.setDeparture("zaragoza");
		dtoIn.setDestination("glasgow");
		
		webTestClient.post()
			.uri("/shortest")
	        .body(Mono.just(dtoIn), FlightInfoDtoIn.class)
			.exchange()
			.expectStatus().is5xxServerError()
			.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
			.expectBody()
			.jsonPath("$.message").exists();
	}
	
	@Test
	public void shouldGetAMethodValidationExceptionMessage() throws Exception {
		
		FlightInfoDtoIn dtoIn = new FlightInfoDtoIn();
		dtoIn.setDeparture(null);
		dtoIn.setDestination("glasgow");
		
		webTestClient.post()
			.uri("/shortest")
	        .body(Mono.just(dtoIn), FlightInfoDtoIn.class)
			.exchange()
			.expectStatus().is4xxClientError()
			.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
			.expectBody()
			.jsonPath("$.message").exists();
	}
	
	@Test
	public void shouldGetAHttpMessageInvalidExceptionMessage() throws Exception {
		
		webTestClient.post()
			.uri("/shortest")
			.exchange()
			.expectStatus().is4xxClientError()
			.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
			.expectBody()
			.jsonPath("$.message").exists();
	}
}
