package com.adidas.flights.flightdata.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Class that maps the document Flight_Info.
 * It uses @JsonIgnore annotation because we don't want the clients to know sensitive information of our core.
 * Also because exposing the Id could let bad people know we're using Mongo and they can try to find vulnerabilities.
 * @author nestis
 *
 */
@Data
@Document(collection = "flight_info")
@ApiModel
public class FlightEntity {

	@Id
	@JsonIgnore
	private String id;
	
	/** Departure airport **/
	@ApiModelProperty(value = "Departure airport.")
	private String departure;
	
	/** Destination airport **/
	@ApiModelProperty(value = "Destination airport.")
	private String destination;
	
	/** Departure time **/
	@ApiModelProperty(value = "Departure time. Always in the same timezone as Arrival Time.")
	private String departureTime;
	
	/** Arrival time **/
	@ApiModelProperty(value = "Arrival time. Always in the same timezone as Departure Time.")
	private String arrivalTime;
	
}
