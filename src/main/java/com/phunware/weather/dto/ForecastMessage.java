package com.phunware.weather.dto;

import com.phunware.weather.rss.enums.WeekDay;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ForecastMessage extends Message {
	private static final long serialVersionUID = 1L;
	private int lowest;
	private int highest;
	private int avg;
	private WeekDay day;
	private int currentHigh;
	private int currentLow;
}
