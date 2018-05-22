package com.adidas.flights.flightinfo.services.domain;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Class that defines a Route between two airports.
 * @author nestis
 *
 */
@Data
@ApiModel
public class RouteDto {
	
	@ApiModelProperty(value = "Time in millis required to fly from departure to destination.")
	/** Route total time */
	Long time;
	
	@ApiModelProperty(value = "Route between departure and destination. Each item on the collection will be a visited ariport.")
	/** Route steps */
	List<String> route;
	
	/**
	 * Class constructor to be used at first stages of the logic, because it will treat the Route param as the departure airport.
	 * @param route Route. 
	 */
	public RouteDto(String route) {
		this.time = 0L;
		this.route = new ArrayList<String>();
		this.route.add(route);
	}
	
	/**
	 * Class constructor.
	 * @param time Time in millis between departure and destination.
	 * @param route Route taken to arrive at destination airport.
	 */
	public RouteDto(Long time, List<String> route) {
		this.time = time;
		this.route = new ArrayList<String>();
		this.route.addAll(route);
	}
}
