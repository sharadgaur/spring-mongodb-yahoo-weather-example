package com.phunware.weather.db.repositories;

import java.util.List;

import com.phunware.weather.db.domain.ZipCode;

public interface ZipCodeRepository {

	public List<ZipCode> findByCiti(String name);

	public ZipCode findOneByZip(String zip);

	public void removeByZip(String zip);

	public void removeByCiti(String citi);

	public void removeAll();

	public void save(ZipCode zip);

	public List<ZipCode> findAll();

}