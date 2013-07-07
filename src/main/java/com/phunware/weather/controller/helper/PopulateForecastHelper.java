package com.phunware.weather.controller.helper;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import com.phunware.weather.db.domain.LowHightAvgTempByZipCode;
import com.phunware.weather.db.repositories.LowHighAvgTempByZipCodeRepository;
import com.phunware.weather.dto.ForecastMessage;
import com.phunware.weather.exceptions.ZipCodeNotFoundException;
import com.phunware.weather.rss.Channel;
import com.phunware.weather.rss.Forecast;
import com.phunware.weather.rss.Item;
import com.phunware.weather.rss.Rss;
import com.phunware.weather.util.YahooWeatherReader;

public class PopulateForecastHelper {
	private YahooWeatherReader yahooService;
	private LowHighAvgTempByZipCodeRepository lowHighAvgTempByZipCodeRepository;
	
	public PopulateForecastHelper(YahooWeatherReader yahooService, LowHighAvgTempByZipCodeRepository lowHighAvgTempByZipCodeRepository) {
		this.yahooService=yahooService;
		this.lowHighAvgTempByZipCodeRepository=lowHighAvgTempByZipCodeRepository;
	}
	
	public ForecastMessage getForecast(String zip) throws JAXBException, IOException, ZipCodeNotFoundException{
		ForecastMessage msg = new ForecastMessage();
		Rss rss = yahooService.getForecast(zip);
		Channel channel = rss.channel;
		Item item = channel.item;
		if(item.forecasts!=null && item.forecasts.size()>0) {
			Forecast forecast = item.forecasts.get(0);
			LowHightAvgTempByZipCode temp = lowHighAvgTempByZipCodeRepository.findOneByZipAndDay(zip, forecast.day);
			if(temp!=null) {
				msg.setAvg(temp.getAvg());
				msg.setCurrentHigh(forecast.high);
				msg.setCurrentLow(forecast.low);
				msg.setDay(temp.getDay());
				msg.setHighest(temp.getHigh());
				msg.setLowest(temp.getLow());
			}else{
				throw new ZipCodeNotFoundException();
			}
			
		}else{
			throw new ZipCodeNotFoundException();
		}
		
		return msg;
	}
}
