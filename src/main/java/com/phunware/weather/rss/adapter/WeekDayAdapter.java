package com.phunware.weather.rss.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import lombok.extern.log4j.Log4j;

import com.phunware.weather.rss.enums.WeekDay;

@Log4j
public class WeekDayAdapter extends XmlAdapter<String, WeekDay> {

	@Override
	public WeekDay unmarshal(String v) throws Exception {
		try {
			return WeekDay.valueOf(v.toUpperCase());
		} catch (Exception e) {
			log.warn("Unknow week day " + v, e);
		}
		return null;
	}

	@Override
	public String marshal(WeekDay v) throws Exception {
		return v != null ? v.toString() : null;
	}

}
