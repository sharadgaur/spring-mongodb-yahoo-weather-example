package com.phunware.weather.db.domain;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
//Store[1]
@Data
@Document
public class ZipCode {
	@Id
	private String id;
	@Indexed
	private String zip;
	private String citi;

}
