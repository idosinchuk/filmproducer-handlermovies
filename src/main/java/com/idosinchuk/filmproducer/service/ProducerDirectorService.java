package com.idosinchuk.filmproducer.service;

import java.util.List;

import com.idosinchuk.filmproducer.dto.ProducerDirectorRequestDTO;
import com.idosinchuk.filmproducer.dto.ProducerDirectorResponseDTO;

/**
 * 
 * Service for producer director
 * 
 * @author Igor Dosinchuk
 *
 */
public interface ProducerDirectorService {

	/**
	 * Retrieve list of all producer directors.
	 *
	 * @return List of {@link ProducerDirectorResponseDTO}
	 */
	List<ProducerDirectorResponseDTO> getAllProducerDirector();

	/**
	 * Add a producer director.
	 * 
	 * @param producerDirectorRequestDTO object to save
	 * 
	 * @return {@link ProducerDirectorResponseDTO}
	 */
	ProducerDirectorResponseDTO addProducerDirector(ProducerDirectorRequestDTO producerDirectorRequestDTO);
}
