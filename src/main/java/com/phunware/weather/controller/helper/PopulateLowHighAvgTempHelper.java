package com.phunware.weather.controller.helper;

import lombok.extern.log4j.Log4j;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.phunware.weather.db.domain.LowHightAvgTempByZipCode;
import com.phunware.weather.db.repositories.LowHighAvgTempByZipCodeRepository;
import com.phunware.weather.db.repositories.WeatherByZipCodeRepository;
import com.phunware.weather.rss.enums.WeekDay;
import com.phunware.weather.util.TemplateReader;

@Log4j
public class PopulateLowHighAvgTempHelper {

	private int rowCount=0;
	private TemplateReader templateReader;
	private LowHighAvgTempByZipCodeRepository lowHighRepo;
	private WeatherByZipCodeRepository weatherRepo;
	
	public PopulateLowHighAvgTempHelper(TemplateReader templateReader,LowHighAvgTempByZipCodeRepository lowHighRepo,WeatherByZipCodeRepository weatherRepo) {
		log.info("init PopulateLowHighAvgTempHelper()");
		this.templateReader=templateReader;
		this.lowHighRepo=lowHighRepo;
		this.weatherRepo=weatherRepo;
		
	}
	
	public void start(){
		rowCount=0;
		String mapper = templateReader.createMapper(null);
		String reducer = templateReader.createReducer(null);
		DBCollection collection = weatherRepo.getCollection();
		MapReduceCommand cmd = new MapReduceCommand(collection,mapper,reducer, null,  MapReduceCommand.OutputType.INLINE, null);
		MapReduceOutput out = collection.mapReduce(cmd);
		BasicDBObject bson = null;;
		for(DBObject object: out.results()) {
			bson = (BasicDBObject)object.get("value");
			if(bson!=null) {
				LowHightAvgTempByZipCode l = new LowHightAvgTempByZipCode();
				l.setAvg(((Double)bson.get("avg")).intValue());
				l.setHigh(((Double)bson.get("high")).intValue());
				l.setLow(((Double)bson.get("low")).intValue());
				l.setZip((String)bson.get("zip"));
				l.setDay(WeekDay.valueOf((String)bson.get("day")));
				lowHighRepo.save(l);
				rowCount++;
			}
		}
		
	}
	
	public int getRowCount() {
		return rowCount;
	}
}
