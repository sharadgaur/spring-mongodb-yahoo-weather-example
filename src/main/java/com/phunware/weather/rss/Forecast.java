package com.phunware.weather.rss;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.phunware.weather.rss.adapter.DateAdapter;
import com.phunware.weather.rss.adapter.WeekDayAdapter;
import com.phunware.weather.rss.enums.WeekDay;

@XmlRootElement
public class Forecast {

	@XmlAttribute
	@XmlJavaTypeAdapter(WeekDayAdapter.class)
	public WeekDay day;

	@XmlAttribute
	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date date;

	@XmlAttribute
	public int low;

	@XmlAttribute
	public int high;

	@XmlAttribute
	public String text;

	@XmlAttribute
	public int code;

}
