package com.idosinchuk.microservice.filmproducer.resource;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProducerDirectorResource extends ResourceSupport {
	private final String content;

	@JsonCreator
	public ProducerDirectorResource(@JsonProperty("content") String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
}