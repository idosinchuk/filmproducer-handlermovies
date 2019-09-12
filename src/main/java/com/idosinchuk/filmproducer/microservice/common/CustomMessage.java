package com.idosinchuk.filmproducer.microservice.common;

import lombok.Data;

@Data
public class CustomMessage {
	private int statusCode;
	private String message;

	public CustomMessage() {
		this.setMessage("Empty");
	}
}
