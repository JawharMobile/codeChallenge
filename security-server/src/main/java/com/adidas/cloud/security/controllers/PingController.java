package com.adidas.cloud.security.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ping controller.
 * Due to security configuration, calling this endpoint means that the user has to be authenticated.
 * So this endpoint will be used by the client app to check if the user JWT token.
 * If a 200 is returned, the token is valid. Otherwise, this endpoint will not be called.
 * @author nestis
 *
 */
@RestController
@RequestMapping("/token")
public class PingController {

	/**
	 * Simply returns a true value.
	 * @return True. The user is authenticated.
	 */
	@GetMapping
	public Boolean getPing() {
		return true;
	}
}
