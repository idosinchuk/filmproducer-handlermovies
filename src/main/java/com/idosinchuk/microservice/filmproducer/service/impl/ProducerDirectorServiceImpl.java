package com.idosinchuk.microservice.filmproducer.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idosinchuk.microservice.filmproducer.dto.ProducerDirectorRequestDTO;
import com.idosinchuk.microservice.filmproducer.dto.ProducerDirectorResponseDTO;
import com.idosinchuk.microservice.filmproducer.entity.ProducerDirectorEntity;
import com.idosinchuk.microservice.filmproducer.repository.ProducerDirectorRepository;
import com.idosinchuk.microservice.filmproducer.service.ProducerDirectorService;

/**
 * Implementation for producer service
 * 
 * @author Igor Dosinchuk
 *
 */
@Service("ProducerDirectorService")
public class ProducerDirectorServiceImpl implements ProducerDirectorService {

	@Autowired
	ProducerDirectorRepository producerDirectorRepo;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * {@inheritDoc}
	 */
	public List<ProducerDirectorResponseDTO> getAllProducerDirector() {

		List<ProducerDirectorEntity> entityResponse = producerDirectorRepo.findAll();

		// Convert Entity response to DTO
		return modelMapper.map(entityResponse, new TypeToken<List<ProducerDirectorResponseDTO>>() {
		}.getType());
	}

	/**
	 * {@inheritDoc}
	 */
	public ProducerDirectorResponseDTO addProducerDirector(ProducerDirectorRequestDTO producerDirectorRequestDTO) {

		// Convert DTO request to entity request
		ProducerDirectorEntity producerDirectorEntity = modelMapper.map(producerDirectorRequestDTO,
				ProducerDirectorEntity.class);

		producerDirectorEntity.setId(producerDirectorRepo.getNextSeriesId().intValue());

		ProducerDirectorEntity entityResponse = producerDirectorRepo.save(producerDirectorEntity);

		// Convert DTO request to entity request
		return modelMapper.map(entityResponse, ProducerDirectorResponseDTO.class);

	}
}
