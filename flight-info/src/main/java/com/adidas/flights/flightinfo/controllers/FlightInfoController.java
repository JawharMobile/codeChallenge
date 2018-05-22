package com.adidas.flights.flightinfo.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adidas.flights.flightinfo.controllers.domain.FlightInfoDtoIn;
import com.adidas.flights.flightinfo.services.FlightInfoService;
import com.adidas.flights.flightinfo.services.domain.RouteDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * FlightsInfo REST Controller
 * @author nestis
 *
 */
@Slf4j
@RestController
@RequestMapping("/")
public class FlightInfoController {

	private FlightInfoService flightInfoService;
	
	@Autowired
	public FlightInfoController(FlightInfoService flightInfoService) {
		this.flightInfoService = flightInfoService;
	}
	
	/**
	 * Returns the shortest route or routes between two airports.
	 * @return Flux<RouteDto> containing the shortest route or routes.
	 */
	@ApiOperation(
			consumes = "application/json",
			produces = "application/json",
			value = "Returns the shortest, in number of stopovers, route or routes between two airports",
			notes="Returns a Flux of RouteDto containing the list of shortests routes between the two aiports passed as parameter")
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Successful retrieval of the shortest route/s"),
		    @ApiResponse(code = 403, message = "Forbidden request. User has no authorization"),
		    @ApiResponse(code = 500, message = "Internal server error...")}
	)
	@PostMapping("/${app.endpoints.shortest:shortest}")
	public Flux<RouteDto> getShortestFlights(
			@ApiParam(name = "dtoIn", value = "Object FlightInfoDtoIn containing departure and destination airports")
			@Valid @RequestBody FlightInfoDtoIn dtoIn) {
		log.debug("REST request received: getShortestFlights()");
		return flightInfoService.getShortestItinerary(dtoIn.getDeparture(), dtoIn.getDestination());
	}
	
	/**
	 * Returns the quickest route or routes between two airports.
	 * @return Flux<RouteDto> containing the quickest route or routes.
	 */
	@ApiOperation(
				consumes = "application/json",
				produces = "application/json",
				value = "Returns the quickest, in time, route or routes between two airports",
				notes="Returns a Flux of RouteDto containing the list of quickests routes between the two aiports passed as parameter")
		@ApiResponses(value = {
			    @ApiResponse(code = 200, message = "Successful retrieval of the quickest route/s"),
			    @ApiResponse(code = 403, message = "Forbidden request. User has no authorization"),
			    @ApiResponse(code = 500, message = "Internal server error...")}
		)
	@PostMapping("/${app.endpoints.quickest:quickest}")
	public Flux<RouteDto> getQuickestFlights(
			@ApiParam(name = "dtoIn", value = "Object FlightInfoDtoIn containing departure and destination airports")
			@Valid @RequestBody FlightInfoDtoIn dtoIn) {
		log.debug("REST request received: getQuickestFlights()");
		return flightInfoService.getQuickestItinerary(dtoIn.getDeparture(), dtoIn.getDestination());
	}
 }
