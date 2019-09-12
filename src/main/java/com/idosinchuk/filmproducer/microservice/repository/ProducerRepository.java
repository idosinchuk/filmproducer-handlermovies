package com.idosinchuk.filmproducer.microservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idosinchuk.filmproducer.microservice.entity.ProducerEntity;

/**
 * Repository for producer
 * 
 * @author Igor Dosinchuk
 *
 */
public interface ProducerRepository extends JpaRepository<ProducerEntity, Integer> {

	ProducerEntity findById(int id);

	@Query(value = "SELECT nextval('producer_id_seq')", nativeQuery = true)
	Long getNextSeriesId();
}
