package com.idosinchuk.microservice.filmproducer.dto;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Response DTO for producer
 * 
 * @author Igor Dosinchuk
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(reference = "ProducerResponse", description = "Model response for manage producer.")
public class ProducerResponseDTO {

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
