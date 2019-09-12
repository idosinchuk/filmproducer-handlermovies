package com.idosinchuk.filmproducer.microservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.idosinchuk.filmproducer.microservice.dto.ProducerRequestDTO;
import com.idosinchuk.filmproducer.microservice.dto.ProducerResponseDTO;

/**
 * 
 * Service for producer
 * 
 * @author Igor Dosinchuk
 *
 */
public interface ProducerService {

	/**
	 * Retrieve list of all producers according to the search criteria.
	 * 
	 * @param pageable object for pagination
	 * @return Page of {@link ProducerResponseDTO}
	 */
	Page<ProducerResponseDTO> getAllProducers(Pageable pageable);

	/**
	 * Find producer by the id.
	 * 
	 * @param id producer identifier
	 * @return {@link ProducerResponseDTO}
	 */
	ProducerResponseDTO findProducerById(int id);

	/**
	 * Add a producer..
	 * 
	 * @param producerRequestDTO object to save
	 * 
	 * @return {@link ProducerResponseDTO}
	 */
	ProducerResponseDTO addProducer(ProducerRequestDTO producerRequestDTO);

	/**
	 * Update the producer.
	 * 
	 * @param producerRequestDTO object to save
	 * 
	 * @return {@link ProducerResponseDTO}
	 */
	ProducerResponseDTO updateProducer(ProducerRequestDTO producerRequestDTO);
}
