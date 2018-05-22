package com.adidas.cloud.zuul.configuration;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Zuul configuration bean.
 * Sets the specific zuul config.
 * Also enables Swagger. We use the annotation here because, if used on the main class, it raises a
 * BeanDefinitionStoreException when launching the tests.
 * @author nestis
 *
 */
@Configuration
@EnableSwagger2
public class ZuulConfiguration {

	/**
	 * Returns the default hystrix fallback bean.
	 * @return Default FallbackProvider.
	 */
	@Bean
	public FallbackProvider defaultZuulFallbackProfider() {
		return new DefaultHystrixFallback();
	}
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.adidas"))
				.build();
	}
}

