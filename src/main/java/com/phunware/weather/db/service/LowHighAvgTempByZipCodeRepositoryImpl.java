package com.phunware.weather.db.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phunware.weather.db.domain.LowHightAvgTempByZipCode;
import com.phunware.weather.db.repositories.LowHighAvgTempByZipCodeRepository;
import com.phunware.weather.rss.enums.WeekDay;

@Transactional
@Repository
public class LowHighAvgTempByZipCodeRepositoryImpl implements
		LowHighAvgTempByZipCodeRepository {

	@Autowired
	MongoTemplate mongoTemplate;
	
	

	@Override
	public LowHightAvgTempByZipCode findOneByZipAndDay(String zip, WeekDay day) {
		Query query = new Query();
		query.addCriteria(Criteria.where("zip").is(zip).and("day").is(day));
		return mongoTemplate.findOne(query, LowHightAvgTempByZipCode.class);
	}
	@Override
	public LowHightAvgTempByZipCode findOneByZip(String zip) {
		Query query = new Query();
		query.addCriteria(Criteria.where("zip").is(zip));
		return mongoTemplate.findOne(query, LowHightAvgTempByZipCode.class);
	}

	@Override
	public void removeByZip(String zip) {
		Query query = new Query();
		query.addCriteria(Criteria.where("zip").is(zip));
		mongoTemplate.remove(query, LowHightAvgTempByZipCode.class);
	}

	
	@Override
	public void removeAll() {
		Query query = new Query();
		mongoTemplate.remove(query,LowHightAvgTempByZipCode.class);
	}

	@Override
	public void save(LowHightAvgTempByZipCode zip) {
		mongoTemplate.save(zip);
	}

	@Override
	public List<LowHightAvgTempByZipCode> findAll() {
		return mongoTemplate.findAll(LowHightAvgTempByZipCode.class);
	}

}
