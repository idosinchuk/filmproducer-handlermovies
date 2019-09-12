package com.idosinchuk.filmproducer.microservice.kafka;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idosinchuk.filmproducer.microservice.dto.ProducerDirectorRequestDTO;
import com.idosinchuk.filmproducer.microservice.dto.ProducerDirectorResponseDTO;
import com.idosinchuk.filmproducer.microservice.dto.ProducerRequestDTO;
import com.idosinchuk.filmproducer.microservice.dto.ProducerResponseDTO;
import com.idosinchuk.filmproducer.microservice.service.ProducerDirectorService;
import com.idosinchuk.filmproducer.microservice.service.ProducerService;

/**
 * Kafka consumer
 * 
 * @author Igor Dosinchuk
 *
 */
@Component
@Transactional
public class KafkaConsumer {
	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

	@Autowired
	ProducerService producerService;

	@Autowired
	ProducerDirectorService producerDirectorService;

	private ProducerResponseDTO producerResponseDTOFromKafka = new ProducerResponseDTO();

	@KafkaListener(topics = "idosinchuk-filmproducer.kafka.post.producer", groupId = "filmproducer")
	public void processPostProducer(String producerJSON) {

		logger.info("received content = '{}'", producerJSON);

		try {
			ObjectMapper mapper = new ObjectMapper();

			ProducerRequestDTO producerRequestDTO = mapper.readValue(producerJSON, ProducerRequestDTO.class);

			ProducerResponseDTO producerResponseDTO = producerService.addProducer(producerRequestDTO);

			logger.info("Success process producer '{}' with topic '{}'", producerResponseDTO.getProducerName(),
					"filmproducer.kafka.post.producer");

		} catch (Exception e) {
			logger.error("An error occurred! '{}'", e.getMessage());
		}
	}

	@KafkaListener(topics = "idosinchuk-filmproducer.kafka.put.producer", groupId = "filmproducer")
	public void processPutProducer(String producerJSON) {

		logger.info("received content = '{}'", producerJSON);

		try {
			ObjectMapper mapper = new ObjectMapper();

			ProducerResponseDTO producer = mapper.readValue(producerJSON, ProducerResponseDTO.class);

			producerResponseDTOFromKafka = producer;

			logger.info("Success process producer '{}' with topic '{}'", producer.getProducerName(),
					"filmproducer.kafka.put.producer");

		} catch (Exception e) {
			logger.error("An error occurred! '{}'", e.getMessage());
		}
	}

	public ProducerResponseDTO getProducerEntityFromKafka(int id) {
		return producerResponseDTOFromKafka;
	}

	@KafkaListener(topics = "idosinchuk-filmproducer.kafka.patch.producer", groupId = "filmproducer")
	public void processPatchProducer(String producerJSON) {

		logger.info("received content = '{}'", producerJSON);

		try {
			ObjectMapper mapper = new ObjectMapper();

			ProducerRequestDTO producerRequestDTO = mapper.readValue(producerJSON, ProducerRequestDTO.class);

			ProducerResponseDTO producerResponseDTO = producerService.updateProducer(producerRequestDTO);

			logger.info("Success process producer '{}' with topic '{}'", producerResponseDTO.getProducerName(),
					"filmproducer.kafka.patch.producer");

		} catch (Exception e) {
			logger.error("An error occurred! '{}'", e.getMessage());
		}
	}

	@KafkaListener(topics = "idosinchuk-filmproducer.kafka.post.producer.director", groupId = "filmproducer")
	public void processPostProducerDirector(String producerDirectorJSON) {

		logger.info("received content = '{}'", producerDirectorJSON);

		try {
			ObjectMapper mapper = new ObjectMapper();

			ProducerDirectorRequestDTO producerDirectorRequestDTO = mapper.readValue(producerDirectorJSON,
					ProducerDirectorRequestDTO.class);

			ProducerDirectorResponseDTO producerDirectorResponseDTO = producerDirectorService
					.addProducerDirector(producerDirectorRequestDTO);

			logger.info("Success process producer director '{}' with topic '{}'",
					producerDirectorResponseDTO.getDirectorName(), "filmproducer.kafka.post.producer");

		} catch (Exception e) {
			logger.error("An error occurred! '{}'", e.getMessage());
		}
	}
}