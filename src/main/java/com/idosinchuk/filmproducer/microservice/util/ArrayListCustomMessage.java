package com.idosinchuk.filmproducer.microservice.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.idosinchuk.filmproducer.microservice.common.CustomMessage;

public class ArrayListCustomMessage {

	public static List<CustomMessage> setMessage(String message, HttpStatus status) {

		CustomMessage cm = new CustomMessage();
		cm.setStatusCode(status.value());
		cm.setMessage(message);

		List<CustomMessage> customMessageList = new ArrayList<>();
		customMessageList.add(cm);

		return customMessageList;
	}
}
