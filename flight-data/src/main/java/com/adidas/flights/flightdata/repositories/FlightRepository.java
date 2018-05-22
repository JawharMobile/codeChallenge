package com.adidas.flights.flightdata.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.adidas.flights.flightdata.entities.FlightEntity;

/**
 * Defines the Spring Data reactive repository to access Flight info collection.
 * @author nestis
 */
public interface FlightRepository extends ReactiveMongoRepository<FlightEntity, String> {

}
