package com.idosinchuk.microservice.filmproducer.dto;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Request DTO for producer
 * 
 * @author Igor Dosinchuk
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(reference = "ProducerRequest", description = "Model request for manage producer.")
public class ProducerRequestDTO {

	@Id
	@ApiModelProperty(value = "Id", example = "1")
	private int id;

	@ApiModelProperty(value = "Director id", example = "Director identifier")
	private String directorId;

	@ApiModelProperty(value = "Producer code", example = "Code of the producer company")
	private String producerCode;

	@ApiModelProperty(value = "Producer name", example = "Name of the producer company")
	private String producerName;

}
