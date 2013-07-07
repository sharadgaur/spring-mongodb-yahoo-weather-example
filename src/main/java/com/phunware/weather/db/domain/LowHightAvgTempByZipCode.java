package com.phunware.weather.db.domain;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.phunware.weather.rss.enums.WeekDay;
//store[3]
@Data
@Document
public class LowHightAvgTempByZipCode {
	@Id
	private String id;
	private int low;
	private int high;
	private int avg;
	@Indexed
	private String zip;
	private WeekDay day;

}
