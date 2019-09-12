package com.idosinchuk.filmproducer.microservice.service.impl;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.idosinchuk.filmproducer.microservice.dto.ProducerRequestDTO;
import com.idosinchuk.filmproducer.microservice.dto.ProducerResponseDTO;
import com.idosinchuk.filmproducer.microservice.entity.ProducerEntity;
import com.idosinchuk.filmproducer.microservice.repository.ProducerRepository;
import com.idosinchuk.filmproducer.microservice.service.ProducerService;

/**
 * Implementation for producer
 * 
 * @author Igor Dosinchuk
 *
 */
@Service("ProducerService")
public class ProducerServiceImpl implements ProducerService {

	@Autowired
	private ProducerRepository producerRepository;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * {@inheritDoc}
	 */
	public Page<ProducerResponseDTO> getAllProducers(Pageable pageable) {

		Page<ProducerEntity> entityResponse = producerRepository.findAll(pageable);

		// Convert Entity response to DTO
		return modelMapper.map(entityResponse, new TypeToken<Page<ProducerResponseDTO>>() {
		}.getType());

	}

	/**
	 * {@inheritDoc}
	 */
	public ProducerResponseDTO findProducerById(int id) {

		ProducerEntity entityResponse = producerRepository.findById(id);

		return modelMapper.map(entityResponse, ProducerResponseDTO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public ProducerResponseDTO addProducer(ProducerRequestDTO producerRequestDTO) {

		ProducerEntity entityRequest = modelMapper.map(producerRequestDTO, ProducerEntity.class);
		entityRequest.setId(producerRepository.getNextSeriesId().intValue());

		ProducerEntity entityResponse = producerRepository.save(entityRequest);

		return modelMapper.map(entityResponse, ProducerResponseDTO.class);

	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public ProducerResponseDTO updateProducer(ProducerRequestDTO producerRequestDTO) {

		ProducerEntity entityRequest = modelMapper.map(producerRequestDTO, ProducerEntity.class);

		ProducerEntity entityResponse = producerRepository.save(entityRequest);

		return modelMapper.map(entityResponse, ProducerResponseDTO.class);

	}
}
