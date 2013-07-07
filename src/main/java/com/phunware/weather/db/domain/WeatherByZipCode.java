package com.phunware.weather.db.domain;

import java.util.Date;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
//Store[2]
@Data
@Document
public class WeatherByZipCode {
	@Id
	private String id;
	@Indexed
	private String zip;
	private Date date;
	private String day;
	private int low;
	private int high;
}
