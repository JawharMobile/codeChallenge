package com.adidas.cloud.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Zuul Server main entry point.
 * Configures the app as a discovery client so Ribbon can be used.
 * Also, configures the Hystrix dashboard to provide a unique access to hystrix streams.
 * @author nestis
 *
 */
@EnableZuulProxy
@EnableHystrixDashboard
@EnableDiscoveryClient
@SpringBootApplication
public class ZuulServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulServerApplication.class, args);
	}
}
