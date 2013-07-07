package com.phunware.weather.db.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.DBCollection;
import com.phunware.weather.db.domain.WeatherByZipCode;
import com.phunware.weather.db.repositories.WeatherByZipCodeRepository;

@Transactional
@Repository
public class WeatherByZipCodeRepositoryImpl implements	WeatherByZipCodeRepository {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public WeatherByZipCode findOneByZip(String zip) {
		Query query = new Query();
		query.addCriteria(Criteria.where("zip").is(zip));
		return mongoTemplate.findOne(query, WeatherByZipCode.class);
	}

	@Override
	public void removeByZip(String zip) {
		Query query = new Query();
		query.addCriteria(Criteria.where("zip").is(zip));
		mongoTemplate.remove(query, WeatherByZipCode.class);

	}

	

	@Override
	public void removeAll() {
		Query query = new Query();
		mongoTemplate.remove(query, WeatherByZipCode.class);

	}

	@Override
	public void save(WeatherByZipCode zip) {
		mongoTemplate.save(zip);
	}

	@Override
	public List<WeatherByZipCode> findAll() {
		return mongoTemplate.findAll(WeatherByZipCode.class);
	}

	@Override
	public DBCollection getCollection() {
		return mongoTemplate.getCollection(mongoTemplate.getCollectionName(WeatherByZipCode.class));
	}
}
