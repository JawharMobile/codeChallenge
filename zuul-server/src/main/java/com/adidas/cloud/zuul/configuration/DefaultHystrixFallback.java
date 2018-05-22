package com.adidas.cloud.zuul.configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

import com.netflix.hystrix.exception.HystrixTimeoutException;

import lombok.extern.slf4j.Slf4j;

/**
 * Default Hystrix fallback for all routes.
 * Simply logs the exception thrown and returns a default JSON body to the client.
 * @author nestis
 *
 */
@Slf4j
public class DefaultHystrixFallback implements FallbackProvider {

	@Override
	public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
		log.debug("Hystrix fallback for route {}", route);
        if (cause instanceof HystrixTimeoutException) {
    			log.debug("Timeout exception:", cause);
            return buildResponse(HttpStatus.GATEWAY_TIMEOUT);
        } else {
			log.debug("Internal error exception:", cause);
            return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}

	@Override
	public String getRoute() {
		return "*";
	}

	private ClientHttpResponse buildResponse(HttpStatus status) {
		return new ClientHttpResponse() {
			
			@Override
			public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
			}
			
			@Override
			public InputStream getBody() throws IOException {
                return new ByteArrayInputStream("{ \"message\" : \"Something went wrong, please try again later\" }".getBytes());
			}
			
			@Override
			public String getStatusText() throws IOException {
				return status.toString();
			}
			
			@Override
			public HttpStatus getStatusCode() throws IOException {
				return status;
			}
			
			@Override
			public int getRawStatusCode() throws IOException {
				return status.value();
			}
			
			@Override
			public void close() {
			}
		};
	}
}
