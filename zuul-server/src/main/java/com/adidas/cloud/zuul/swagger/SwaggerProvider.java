package com.adidas.cloud.zuul.swagger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

/**
 * Defines a SwaggerResourcesProvider.
 * All the microservices exposing a swagger endpoint will be added here.
 * @author nestis
 *
 */
@Component
@Primary
public class SwaggerProvider implements SwaggerResourcesProvider {

	@Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<SwaggerResource>();
        resources.add(swaggerResource("flight-info", "/flightsInfo/v2/api-docs", "2.0"));
        return resources;
    }
	
	private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
