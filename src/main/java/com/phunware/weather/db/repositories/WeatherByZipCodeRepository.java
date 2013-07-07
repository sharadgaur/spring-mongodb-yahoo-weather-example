package com.phunware.weather.db.repositories;

import java.util.List;

import com.mongodb.DBCollection;
import com.phunware.weather.db.domain.WeatherByZipCode;

public interface WeatherByZipCodeRepository {

	public WeatherByZipCode findOneByZip(String zip);

	public void removeByZip(String zip);

	public void removeAll();

	public void save(WeatherByZipCode zip);

	public List<WeatherByZipCode> findAll();
	
	public DBCollection getCollection();

}
