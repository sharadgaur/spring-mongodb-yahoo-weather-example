package com.phunware.weather.db.repositories;

import java.util.List;

import com.phunware.weather.db.domain.LowHightAvgTempByZipCode;
import com.phunware.weather.rss.enums.WeekDay;

public interface LowHighAvgTempByZipCodeRepository {
	
	public LowHightAvgTempByZipCode findOneByZipAndDay(String zip, WeekDay day);
	
	public LowHightAvgTempByZipCode findOneByZip(String zip);

	public void removeByZip(String zip);

	
	public void removeAll();

	public void save(LowHightAvgTempByZipCode zip);

	public List<LowHightAvgTempByZipCode> findAll();
}
