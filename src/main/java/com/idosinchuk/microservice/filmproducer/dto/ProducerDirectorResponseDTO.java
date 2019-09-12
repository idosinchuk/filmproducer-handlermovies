package com.idosinchuk.microservice.filmproducer.dto;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Response DTO for producer director
 * 
 * @author Igor Dosinchuk
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(reference = "ProducerDirectorResponse", description = "Model response for manage producer director.")
public class ProducerDirectorResponseDTO {

	@Id
	@ApiModelProperty(value = "Id", example = "1")
	private int id;

	@ApiModelProperty(value = "Director name", example = "Director name")
	private String directorName;

}
