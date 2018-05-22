package com.adidas.flights.flightinfo.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.adidas.flights.flightinfo.controllers.domain.FlightInfoDtoIn;
import com.adidas.flights.flightinfo.services.domain.RouteDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = {
	"flightData.ribbon.listOfServers=http://localhost:27700",
	"eureka.client.enabled=false"
})
public class FlightInfoControllerIntegrationTest {
	
	@Autowired
	private WebTestClient webTestClient;
	
	  @Autowired
	  protected MockMvc mockMvc;

	private MockWebServer server;
	
	@Before
	public void setUp() throws IOException {
        server = new MockWebServer();
        server.start(27700);
	}
	
	@After
	public void shutdown() throws Exception {
		server.shutdown();
	}
	
	@Test
	public void shouldReturnQuickestFlights() throws Exception {
		String content ="[{\"departure\":\"zaragoza\", \"destination\":\"london\", "
				+ "\"departureTime\":\"12:00\", \"arrivalTime\":\"0:40\"}]";
		server.enqueue(new MockResponse().addHeader("Content-Type", "application/json").setBody(content));
		
		FlightInfoDtoIn dtoIn = new FlightInfoDtoIn();
		dtoIn.setDeparture("zaragoza");
		dtoIn.setDestination("london");
		
		MvcResult result = mockMvc.perform(post("/quickest")
	            .contentType(MediaType.APPLICATION_JSON_UTF8)
	            .content(asJsonString(dtoIn))
	            .accept(MediaType.APPLICATION_JSON))
				.andExpect(request().asyncStarted())
	            .andExpect(status().isOk())
	            .andReturn();
		
		List<RouteDto> response = (List<RouteDto>) result.getAsyncResult();
		assertThat(response, hasSize(1));
	}
	
	@Test
	public void shouldReturnShortestFlights() throws URISyntaxException {
		String content ="[{\"departure\":\"zaragoza\", \"destination\":\"london\", "
				+ "\"departureTime\":\"12:00\", \"arrivalTime\":\"0:40\"}]";
		server.enqueue(new MockResponse().addHeader("Content-Type", "application/json").setBody(content));
		
		FlightInfoDtoIn dtoIn = new FlightInfoDtoIn();
		dtoIn.setDeparture("zaragoza");
		dtoIn.setDestination("london");
		
		webTestClient.post()
			.uri("/quickest")
	        .body(Mono.just(dtoIn), FlightInfoDtoIn.class)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
			.expectBody()
			.jsonPath("$[0].time").isEqualTo("45600000")
			.jsonPath("$[0].route").isArray();
	}

	@Test
	public void shouldReturnA400Status() throws URISyntaxException {
		FlightInfoDtoIn dtoIn = new FlightInfoDtoIn();
		dtoIn.setDeparture(null);
		dtoIn.setDestination("london");
		
		webTestClient.post()
			.uri("/quickest")
	        .body(Mono.just(dtoIn), FlightInfoDtoIn.class)
			.exchange()
			.expectStatus().is4xxClientError();
	}
	
	@Test
	public void shouldReturnA500Status() throws URISyntaxException {
		server.enqueue(new MockResponse().addHeader("Content-Type", "application/json").setResponseCode(500));
		
		FlightInfoDtoIn dtoIn = new FlightInfoDtoIn();
		dtoIn.setDeparture("zaragoza");
		dtoIn.setDestination("london");
		
		webTestClient.post()
			.uri("/quickest")
	        .body(Mono.just(dtoIn), FlightInfoDtoIn.class)
			.exchange()
			.expectStatus().is5xxServerError();
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        final ObjectMapper mapper = new ObjectMapper();
	        final String jsonContent = mapper.writeValueAsString(obj);
	        System.out.println(jsonContent);
	        return jsonContent;
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
}
