package com.idosinchuk.filmproducer;

import javax.servlet.Filter;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class FilmproducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilmproducerApplication.class, args);
	}

	// To Support Cache API
	@Bean
	public Filter filter() {
		return new ShallowEtagHeaderFilter();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Configuration
	public class WebMvcConfig implements WebMvcConfigurer {
		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
			registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

			registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		}
	}

}
