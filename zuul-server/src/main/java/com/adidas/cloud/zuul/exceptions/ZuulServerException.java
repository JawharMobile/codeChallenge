package com.adidas.cloud.zuul.exceptions;

import lombok.Data;

/**
 * ZuulServerException. Used to handle all the exceptions raised inside the Zuul application. 
 * @author nestis
 *
 */
@Data
public class ZuulServerException extends RuntimeException {

	/**
	 * Source exception.
	 */
	private Throwable exception;
	
	/**
	 * Exception message.
	 */
	private String message;
}
