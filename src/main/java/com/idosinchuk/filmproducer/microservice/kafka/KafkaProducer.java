package com.idosinchuk.filmproducer.microservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idosinchuk.filmproducer.microservice.dto.ProducerDirectorRequestDTO;
import com.idosinchuk.filmproducer.microservice.dto.ProducerRequestDTO;

/**
 * Kafka producer
 * 
 * @author Igor Dosinchuk
 *
 */
@Service
public class KafkaProducer {

	private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void postProducer(String topic, String groupId, ProducerRequestDTO producerRequestDTO) {

		try {
			logger.info("Sending data to kafka = '{}' with topic '{}'", producerRequestDTO.getProducerName(), topic);

			ObjectMapper mapper = new ObjectMapper();

			kafkaTemplate.send(topic, groupId, mapper.writeValueAsString(producerRequestDTO));

		} catch (Exception e) {
			logger.error("An error occurred! '{}'", e.getMessage());
		}
	}

	public void postProducerDirector(String topic, String groupId,
			ProducerDirectorRequestDTO producerDirectorRequestDTO) {

		try {

			logger.info("Sending data to kafka = '{}' with topic '{}'", producerDirectorRequestDTO.getDirectorName(),
					topic);

			ObjectMapper mapper = new ObjectMapper();

			kafkaTemplate.send(topic, groupId, mapper.writeValueAsString(producerDirectorRequestDTO));

		} catch (Exception e) {
			logger.error("An error occurred! '{}'", e.getMessage());
		}
	}
}