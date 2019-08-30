package com.idosinchuk.filmproducer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.rest.core.annotation.RestResource;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity for Producer director
 * 
 * @author Igor Dosinchuk
 *
 */
@Entity
@NoArgsConstructor
@Data
@RestResource
@Table(name = "producer_director", schema = "public", catalog = "filmproducer")
public class ProducerDirectorEntity {

	@Id
	@Column(name = "id")
	private int id;

	@Column(name = "director_name")
	private String directorName;

}
