package com.phunware.weather.controller.helper;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import lombok.extern.log4j.Log4j;

import com.phunware.weather.db.domain.WeatherByZipCode;
import com.phunware.weather.db.domain.ZipCode;
import com.phunware.weather.db.repositories.WeatherByZipCodeRepository;
import com.phunware.weather.db.repositories.ZipCodeRepository;
import com.phunware.weather.rss.Channel;
import com.phunware.weather.rss.Forecast;
import com.phunware.weather.rss.Rss;
import com.phunware.weather.util.YahooWeatherReader;

@Log4j
public class PopulateWeatheHighLowHelper  {

	private int rowCount = 0;
	
	private WeatherByZipCodeRepository weatherRepo;
	
	private ZipCodeRepository zipRepo;
	
	private YahooWeatherReader yahooService;

	private Thread thread;
	
	private boolean interrupted;
	
	public PopulateWeatheHighLowHelper(ZipCodeRepository zipRepo, WeatherByZipCodeRepository repo,YahooWeatherReader yahooService) {
		log.info("init PopulateWeatheHighLowHelper()");
		this.weatherRepo = repo;
		this.zipRepo=zipRepo;
		this.yahooService=yahooService;
	}
	
	
	/**
	 * Start Weather Service to update Store[2]
	 * @throws JAXBException
	 * @throws IOException
	 */
	public void start() throws JAXBException, IOException {
		if(!isRunning()) {
			log.info("Starting PopulateWeatheHighLowHelper() in thread");
			rowCount = 0;
			interrupted=false;
			thread = new Thread( new Runnable() {
				public void run() {
					runIt();
				}
			});
			thread.start();
		
		}	
			
	}
	
	/**
	 * Check if  Weather service is running or not
	 * @return
	 */
	public boolean isRunning(){
		return (thread!=null && thread.isAlive());
	}
	
	/**
	 * Stop Thread
	 */
	public void stop() {
		thread.interrupt();
	}
	
	/**
	 * Update Store[2] with low and high temp
	 */
	protected void runIt(){
			try {
			List<ZipCode> allZipCodes = zipRepo.findAll();
			Rss rss = null;
			Channel channel =null;
			for(ZipCode zip : allZipCodes) {
				if(Thread.interrupted()) {
					throw new InterruptedException("PopulateWeatheHighLowHelper  is stopped");
				}
				rss = yahooService.getForecast(zip.getZip());
				channel =rss.channel;
				if(channel.item.forecasts!=null && channel.item.forecasts.size()>0) {
					Forecast forecast = channel.item.forecasts.get(0);
					if(forecast!=null) {
						WeatherByZipCode code = new WeatherByZipCode();
						code.setDate(forecast.date);
						code.setHigh(forecast.high);
						code.setLow(forecast.low);
						code.setZip(zip.getZip());
						code.setDay(forecast.day.name());
						weatherRepo.save(code);
						rowCount++;
					}
				}
			}
		} catch (JAXBException e) {
			log.error("JAXB Exception ", e);
		} catch (IOException e) {
			log.error("IO Exception ", e);
		} catch(InterruptedException e) {
			log.error(e);
			interrupted=true;
		}
	}
	public int getRowCount() {
		return rowCount;
	}
	
	public boolean isInterrupted() {
		return interrupted;
	}
}
