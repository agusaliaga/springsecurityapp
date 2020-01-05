package com.agusaliaga;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * After the Docket bean is defined, its select() method returns an instance of ApiSelectorBuilder,
 * which provides a way to control the endpoints exposed by Swagger.
 * Predicates for selection of RequestHandlers can be configured with the help of 
 * RequestHandlerSelectors and PathSelectors. Using any() for both will make documentation 
 * for your entire API available through Swagger.
 **/

@Configuration
@EnableSwagger2
public class SpringFoxConfig {
	
	@Value("${agusaliaga.app.jwtSecret}")
	private String jwtSecret;
	
	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(Predicates.not(PathSelectors.regex("/error.*")))
          .build()
          .securitySchemes(Arrays.asList(apiKey()))
          .apiInfo(apiInfo());
    }
	
	private ApiKey apiKey() {
		return new ApiKey("Bearer", "Authorization", "header");
	}
	
	@Bean
	public SecurityConfiguration security() {
			return SecurityConfigurationBuilder.builder()
					.clientId("test-app-client-id")
					.clientSecret(jwtSecret)
					.appName("Spring Boot Security")
					.scopeSeparator(",")
					.additionalQueryStringParams(null)
					.useBasicAuthenticationWithAccessCodeGrant(false)
					.build();
	}

	@Bean
	ApiInfo apiInfo() {
		final ApiInfoBuilder builder = new ApiInfoBuilder();
	    builder.title("Spring Boot Security API").version("1.0").license("(C) Agustina Aliaga")
	            .description("List of all endpoints used in API");
	    return builder.build();
	}
}