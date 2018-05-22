package com.adidas.flights.flightinfo.controllers;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.adidas.flights.flightinfo.exceptions.FlightInfoRestException;

import lombok.extern.slf4j.Slf4j;

/**
 * RestControllerAdvice.
 * It handles the exceptions raised by all the app rest controller and return a FlightInfoRestException.
 * The purpose is to provide only one type of exception to the client.
 * @author nestis
 *
 */
@RestControllerAdvice
@Slf4j
public class FlightInfoControllerAdvice {

	/**
	 * Handles a RuntimeException.
	 * It converts the exception into a FlightInfoRestException and sets an INTERNAL_SERVER_ERROR response status.
	 * @param ex RuntimeException raised.
	 * @return FlightInfoRestException.
	 */
	@ExceptionHandler(value = RuntimeException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public FlightInfoRestException handleRuntime(RuntimeException ex) {
		log.error("RuntimeException handled:", ex);
		return new FlightInfoRestException(new Date().getTime(), "Ups! We messed things up while handling your request! Please try again later :)");
	}
	
	/**
	 * Handles a MethodArgumentNotValidException and HttpMessageNotReadableException, and sets a BAD_REQUEST response status.	
	 * @param ex MethodArgumentNotValidException raised.
	 * @return FlightInfoRestException
	 */
	@ExceptionHandler({ MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public FlightInfoRestException handleArgumentInvalid(Exception ex) {
		log.error("InvalidArgument handled:", ex);
		return new FlightInfoRestException(new Date().getTime(), "Posted data is invalid! Please try again! :)");
	}
	
	/**
	 * Handles a checked Exception, converts it into a FlightInfoRestException and sets an INTERNAL_SERVER_ERROR response status.
	 * @param ex Exception raised.
	 * @return FlightInfoRestException.
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public FlightInfoRestException handleException(Exception ex) {
		log.error("Exception handled:", ex);
		return new FlightInfoRestException(new Date().getTime(), "Ups! We messed things up while handling your request! Please try again later :)");
	}
}
