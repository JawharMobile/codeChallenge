package com.adidas.flights.flightinfo.controllers.domain;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Defines a DTO_IN for flights endpoint.
 * @author nestis
 *
 */
@Data
@ApiModel
public class FlightInfoDtoIn {

	@ApiModelProperty(required = true, value = "Departure airport.")
	@NotNull
	/** Departure airport **/
	private String departure;

	@ApiModelProperty(required = true, value = "Destination airport.")
	@NotNull
	/** Destination airport **/
	private String destination;
}
