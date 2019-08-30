package com.idosinchuk.filmproducer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.idosinchuk.filmproducer.entity.ProducerDirectorEntity;

/**
 * Repository for producer director
 * 
 * @author Igor Dosinchuk
 *
 */
@RepositoryRestResource(path = "/api/producer/director")
public interface ProducerDirectorRepository extends JpaRepository<ProducerDirectorEntity, Integer> {

	@Query(value = "SELECT nextval('producer_director_id_seq')", nativeQuery = true)
	Long getNextSeriesId();
}
