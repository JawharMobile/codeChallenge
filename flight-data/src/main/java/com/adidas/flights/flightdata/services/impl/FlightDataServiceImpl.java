package com.adidas.flights.flightdata.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adidas.flights.flightdata.entities.FlightEntity;
import com.adidas.flights.flightdata.repositories.FlightRepository;
import com.adidas.flights.flightdata.services.FlightDataService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * FlightDataService implementation.
 * @author nestis
 *
 */
@Slf4j
@Service
public class FlightDataServiceImpl implements FlightDataService {

	private FlightRepository flightRepository;
	
	@Autowired
	public FlightDataServiceImpl(FlightRepository flightRepository) {
		this.flightRepository = flightRepository;
	}

	@Override
	public Flux<FlightEntity> getFlights() {
		log.debug("Service request received: getFlights()");
		return flightRepository.findAll();
	}
}
