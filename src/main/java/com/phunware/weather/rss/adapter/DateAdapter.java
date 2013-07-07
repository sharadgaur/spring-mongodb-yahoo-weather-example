package com.phunware.weather.rss.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import lombok.extern.log4j.Log4j;

@Log4j
public class DateAdapter extends XmlAdapter<String, Date> {

	protected SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");

	@Override
	public String marshal(Date v) throws Exception {
		return dateFormat.format(v);
	}

	@Override
	public Date unmarshal(String v) throws Exception {

		try {
			return dateFormat.parse(v);
		} catch (Exception e) {
			log.warn("Unknow date format " + v, e);
			return null;
		}
	}

}
