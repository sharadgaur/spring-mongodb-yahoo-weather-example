package com.phunware.weather.rss;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Channel {

	@XmlElement
	public String title;

	@XmlElement
	public String link;

	@XmlElement
	public String language;

	@XmlElement
	public String description;

	@XmlElement
	public Item item;

}
