package com.idosinchuk.filmproducer.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.idosinchuk.filmproducer.common.CustomMessage;
import com.idosinchuk.filmproducer.dto.ProducerRequestDTO;
import com.idosinchuk.filmproducer.dto.ProducerResponseDTO;
import com.idosinchuk.filmproducer.kafka.KafkaConsumer;
import com.idosinchuk.filmproducer.kafka.KafkaProducer;
import com.idosinchuk.filmproducer.resource.MultiResource;
import com.idosinchuk.filmproducer.service.ProducerService;
import com.idosinchuk.filmproducer.util.ArrayListCustomMessage;
import com.idosinchuk.filmproducer.util.CustomErrorType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Controller for producer
 * 
 * @author Igor Dosinchuk
 *
 */
@RestController
@Api(value = "API Rest for manage producers.")
@RequestMapping(value = "/api/v1/producer")
public class ProducerController {

	public static final Logger logger = LoggerFactory.getLogger(ProducerController.class);

	@Autowired
	ProducerService producerService;

	@Autowired
	KafkaProducer kafkaProducer;

	@Autowired
	KafkaConsumer kafkaConsumer;

	@Value("${spring.kafka.consumer.group-id}")
	String kafkaGroupId;

	@Value("${filmproducer.kafka.post.producer}")
	String postProducerTopic;

	@Value("${filmproducer.kafka.put.producer}")
	String putProducerTopic;

	@Value("${filmproducer.kafka.patch.producer}")
	String patchProducerTopic;

	/**
	 * Retrieve list of all producers according to the search criteria.
	 * 
	 * @param pageable paging fields
	 * @return ResponseEntity with paged list of all producers, headers and status
	 */
	@GetMapping
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@ApiOperation(value = "Retrieve list of all producers according to the search criteria.")
	public ResponseEntity<?> getAllProducers(Pageable pageable, PagedResourcesAssembler pagedResourcesAssembler,
			@RequestHeader("User-Agent") String userAgent) {

		logger.info("Fetching all producers");

		Page<ProducerResponseDTO> producer = null;

		try {
			// Find producers in DB with paging filters
			producer = producerService.getAllProducers(pageable);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		MultiValueMap<String, String> headers = new HttpHeaders();
		headers.put(HttpHeaders.USER_AGENT, Arrays.asList(userAgent));

		PagedResources<MultiResource> pagedResources = pagedResourcesAssembler.toResource(producer);

		return new ResponseEntity<>(pagedResources, headers, HttpStatus.OK);
	}

	/**
	 * Retrieve producer by the id.
	 * 
	 * @param id producer identifier
	 * @return ResponseEntity with status and producerResponseDTO
	 */
	@GetMapping(value = "/{id}")
	@ResponseBody
	@ApiOperation(value = "Retrieve producer by the id.")
	public ResponseEntity<?> getProducerById(@PathVariable("id") int id) {

		logger.info("Fetching producer with ID {}", id);

		ProducerResponseDTO producer = null;

		try {
			// Check if producer exists in kafkas
			producer = kafkaConsumer.getProducerEntityFromKafka(id);

			// Otherwise search producer in BD by producerId
			if (producer.getId() == 0)
				producer = producerService.findProducerById(id);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(producer, HttpStatus.OK);
	}

	/**
	 * Add a producer.
	 * 
	 * @param producerRequestDTO object to save
	 * @return ResponseEntity with status and producerResponseDTO
	 */
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = "Add a producer.")
	public ResponseEntity<?> addProducer(@RequestBody ProducerRequestDTO producerRequestDTO) {

		logger.info(("Process add new producer"));

		Resources<CustomMessage> resource = null;

		try {

			producerService.addProducer(producerRequestDTO);

			List<CustomMessage> customMessageList = ArrayListCustomMessage.setMessage("Created new producer",
					HttpStatus.CREATED);

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(ProducerController.class).withSelfRel());
			resource.add(linkTo(ProducerDirectorController.class).withRel("producer_director"));

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(resource, HttpStatus.OK);
	}

	/**
	 * Add a producer in kafka before and in the DB.
	 * 
	 * @param producerRequestDTO object to save
	 * @return ResponseEntity with status and producerResponseDTO
	 */
	@PostMapping(value = "/layered", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = "Add a producer in kafka before and in the DB.")
	public ResponseEntity<?> addLayeredProducer(@RequestBody ProducerRequestDTO producerRequestDTO) {

		logger.info(("Process add new producer"));

		Resources<CustomMessage> resource = null;

		try {

			// Call kafkaProducer to save the producer into kafka
			kafkaProducer.postProducer(postProducerTopic, kafkaGroupId, producerRequestDTO);

			List<CustomMessage> customMessageList = ArrayListCustomMessage.setMessage("Created new producer layered",
					HttpStatus.ACCEPTED);

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(ProducerController.class).withSelfRel());
			resource.add(linkTo(ProducerDirectorController.class).withRel("producer_director"));

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(resource, HttpStatus.OK);
	}

	/**
	 * The PUT method requests that the attached entity be stored under the request
	 * URI provided. If the request URI refers to an existing resource, the attached
	 * entity SHOULD be considered as a modified version of the one that resides on
	 * the source server. If the request URI does not point to an existing resource,
	 * and that URI can be defined as a new resource by the requesting user agent,
	 * the source server can create the resource with that URI.
	 * 
	 * @param id                 producer identifier
	 * @param producerRequestDTO object to update
	 * @return ResponseEntity with resource and status
	 */
	@PutMapping(value = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<?> putProducer(@PathVariable("id") int id,
			@RequestBody ProducerRequestDTO producerRequestDTO) {

		return putAndPatch(producerRequestDTO, id, 0);

	}

	/**
	 * El método PATCH solicita que se aplique un conjunto de cambios descritos en
	 * la entidad de solicitud al recurso identificado por el URI de Solicitud.
	 * 
	 * @param id                 producer identifier
	 * @param producerRequestDTO object to update
	 * @return ResponseEntity with resource and status
	 */
	@PatchMapping(value = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = "Update the producer.")
	public ResponseEntity<?> updateProducer(@PathVariable("id") int id,
			@RequestBody ProducerRequestDTO producerRequestDTO) {

		return putAndPatch(producerRequestDTO, id, 1);

	}

	@SuppressWarnings("rawtypes")
	private ResponseEntity<Resources> putAndPatch(ProducerRequestDTO producerRequestDTO, int id, int mode) {

		logger.info("Process '{}' producer", (mode == 0 ? "put" : "patch"));

		Resources<CustomMessage> resource = null;

		try {

			List<CustomMessage> customMessageList = null;

			// Find producer by id for check if exists in DB
			ProducerResponseDTO producerResponseDTO = producerService.findProducerById(id);

			// If exists
			if (producerResponseDTO != null) {

				customMessageList = ArrayListCustomMessage
						.setMessage((mode == 0 ? "Put" : "Patch") + " producer process", HttpStatus.OK);

				// Set the producer id to the wanted request object
				producerRequestDTO.setId(id);

				// If mode is patch
				if (mode != 0)
					// The producer's code will always be the same, so we do not allow it to be
					// updated, for them we overwrite the field with the original value.
					producerRequestDTO.setProducerCode(producerResponseDTO.getProducerCode());

				kafkaProducer.postProducer((mode == 0 ? putProducerTopic : patchProducerTopic), kafkaGroupId,
						producerRequestDTO);

			} else {
				customMessageList = ArrayListCustomMessage.setMessage("Producer Id" + id + " Not Found!",
						HttpStatus.BAD_REQUEST);

				resource = new Resources<>(customMessageList);
				resource.add(linkTo(ProducerController.class).withSelfRel());

				return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
			}

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(ProducerController.class).slash(id).withSelfRel());
			resource.add(linkTo(ProducerDirectorController.class).withRel("producer_director"));

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			CustomErrorType.returnResponsEntityError(e.getMessage());

		}

		return new ResponseEntity<>(resource, HttpStatus.OK);
	}
}
