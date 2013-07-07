package com.phunware.weather.rss;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Item {

	@XmlElement
	public String title;

	@XmlElement(namespace = "http://xml.weather.yahoo.com/ns/rss/1.0", name = "forecast")
	public List<Forecast> forecasts;

}
