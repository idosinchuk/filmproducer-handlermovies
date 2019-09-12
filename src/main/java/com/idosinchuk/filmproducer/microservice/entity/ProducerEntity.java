package com.idosinchuk.filmproducer.microservice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity for Producer
 * 
 * @author Igor Dosinchuk
 *
 */
@Entity
@NoArgsConstructor
@Data
@Table(name = "producer", schema = "public", catalog = "filmproducer")
public class ProducerEntity {

	@Id
	@Column(name = "id")
	private int id;

	@Column(name = "director_id")
	private String directorId;

	@Column(name = "producer_code")
	private String producerCode;

	@Column(name = "producer_name")
	private String producerName;

}
