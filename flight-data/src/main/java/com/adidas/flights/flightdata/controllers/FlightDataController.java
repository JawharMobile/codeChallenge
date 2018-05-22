package com.adidas.flights.flightdata.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adidas.flights.flightdata.entities.FlightEntity;
import com.adidas.flights.flightdata.services.FlightDataService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * REST Controller that exposes access to FlightDataService.
 * It simply returns the flights on db, but it can be also used as CRUD Controller for that entity.
 * @author nestis
 *
 */
@Slf4j
@RestController
@RequestMapping("/${app.endpoints.flightData:flightsData}")
public class FlightDataController {

	private FlightDataService flightDataService;
	
	@Autowired
	public FlightDataController(FlightDataService flightdataService) {
		this.flightDataService = flightdataService;
	}
	
	/**
	 * Returns all the flights.
	 * @return Flux emitting all the flights.
	 */
	@ApiOperation(
			consumes = "application/json",
			produces = "application/json",
			value = "Returns the flight data present in db.",
			notes="Returns a Flux of FlightEntity containing the flights stored in database.")
	@ApiResponses(value = {
		    @ApiResponse(code = 200, message = "Successful retrieval of the flight data"),
		    @ApiResponse(code = 403, message = "Forbidden request. User has no authorization"),
		    @ApiResponse(code = 500, message = "Internal server error...")}
	)
	@GetMapping
	public Flux<FlightEntity> getAllFlights() {
		log.debug("REST request received => getAllFlights()");
		return this.flightDataService.getFlights();
	}
}
