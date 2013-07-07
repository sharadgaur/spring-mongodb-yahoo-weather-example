package com.phunware.weather.db.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phunware.weather.db.domain.ZipCode;
import com.phunware.weather.db.repositories.ZipCodeRepository;

@Repository
@Transactional
public class ZipCodeRepositoryImpl implements ZipCodeRepository {

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public List<ZipCode> findByCiti(String name) {
		Query query = new Query();
		query.addCriteria(Criteria.where("citi").is(name));
		return mongoTemplate.find(query, ZipCode.class);
	}

	@Override
	public ZipCode findOneByZip(String zip) {
		Query query = new Query();
		query.addCriteria(Criteria.where("zip").is(zip));
		return mongoTemplate.findOne(query, ZipCode.class);
	}

	@Override
	public void removeAll() {
		Query query = new Query();
		mongoTemplate.remove(query, ZipCode.class);

	}

	@Override
	public void removeByCiti(String citi) {
		Query query = new Query();
		query.addCriteria(Criteria.where("citi").is(citi));
		mongoTemplate.remove(query, ZipCode.class);

	}

	@Override
	public void removeByZip(String zip) {
		Query query = new Query();
		query.addCriteria(Criteria.where("zip").is(zip));
		mongoTemplate.remove(query, ZipCode.class);

	}

	@Override
	public void save(ZipCode zip) {
		mongoTemplate.save(zip);
	}

	@Override
	public List<ZipCode> findAll() {
		return mongoTemplate.findAll(ZipCode.class);
	}

}
