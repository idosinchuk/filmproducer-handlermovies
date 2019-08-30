package com.idosinchuk.filmproducer.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idosinchuk.filmproducer.common.CustomMessage;
import com.idosinchuk.filmproducer.dto.ProducerDirectorRequestDTO;
import com.idosinchuk.filmproducer.dto.ProducerDirectorResponseDTO;
import com.idosinchuk.filmproducer.kafka.KafkaProducer;
import com.idosinchuk.filmproducer.resource.ProducerDirectorResource;
import com.idosinchuk.filmproducer.service.ProducerDirectorService;
import com.idosinchuk.filmproducer.util.CustomErrorType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Controller for producer director
 * 
 * @author Igor Dosinchuk
 *
 */
@Controller
@Api(value = "API Rest for manage directors.")
@RequestMapping("/admin/v1/producerdirector")
public class ProducerDirectorController {
	public static final Logger logger = LoggerFactory.getLogger(ProducerDirectorController.class);

	@Autowired
	ProducerDirectorService producerDirectorService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	KafkaProducer kafkaProducer;

	@Value("${spring.kafka.consumer.group-id}")
	String kafkaGroupId;

	@Value("${filmproducer.kafka.post.producer.director}")
	String postProducerDirectorTopic;

	/**
	 * Retrieve list of all producer directors.
	 * 
	 * @return response with resource and status
	 */
	@SuppressWarnings("rawtypes")
	@GetMapping
	@ResponseBody
	@ApiOperation(value = "Retrieve list of all producer directors.")
	public ResponseEntity<?> getAllProducerDirectors() {

		List<ProducerDirectorResponseDTO> producerDirector = null;

		try {
			producerDirector = producerDirectorService.getAllProducerDirector();

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		Resources<ProducerDirectorResource> res = new Resources(producerDirector);
		res.add(linkTo(ProducerDirectorController.class).withSelfRel());
		res.add(linkTo(ProducerController.class).withRel("producer"));

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	/**
	 * Add a producer director.
	 * 
	 * @param producerDirectorRequestDTO object to save
	 * @return ResponseEntity with status and producerDirectorResponseDTO
	 */
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = "Add a producer director.")
	public ResponseEntity<?> addProducerDirector(@RequestBody ProducerDirectorRequestDTO producerDirectorRequestDTO) {

		logger.info(("Process add new producer director"));
		CustomMessage customMessage = new CustomMessage();

		try {
			kafkaProducer.postProducerDirector(postProducerDirectorTopic, kafkaGroupId, producerDirectorRequestDTO);

			customMessage.setStatusCode(HttpStatus.OK.value());
			customMessage.setMessage("Created new producer director");

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(customMessage, HttpStatus.OK);
	}
}
