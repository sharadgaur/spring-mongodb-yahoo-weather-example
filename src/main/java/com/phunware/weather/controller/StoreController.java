package com.phunware.weather.controller;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.mongodb.MongoException;
import com.phunware.weather.controller.helper.PopulateForecastHelper;
import com.phunware.weather.controller.helper.PopulateLowHighAvgTempHelper;
import com.phunware.weather.controller.helper.PopulateWeatheHighLowHelper;
import com.phunware.weather.controller.helper.PopulateZipCodeHelper;
import com.phunware.weather.db.repositories.LowHighAvgTempByZipCodeRepository;
import com.phunware.weather.db.repositories.WeatherByZipCodeRepository;
import com.phunware.weather.db.repositories.ZipCodeRepository;
import com.phunware.weather.dto.Message;
import com.phunware.weather.dto.StoreStatus;
import com.phunware.weather.exceptions.DataFileNotFoundException;
import com.phunware.weather.exceptions.ZipCodeNotFoundException;
import com.phunware.weather.util.YahooWeatherReader;

@Controller
@Log4j
@RequestMapping("/")
public class StoreController {

	@Autowired
	Gson gson;

	@Autowired
	ZipCodeRepository zipRepo;
	
	@Autowired
	WeatherByZipCodeRepository weatherByZipCodeRepository;
	
	@Autowired
	PopulateZipCodeHelper zipCodeHelper;
	
	@Autowired
	LowHighAvgTempByZipCodeRepository lowHighAvgTempByZipCodeRepository;
	
	@Autowired
	PopulateForecastHelper populateForecastHelper;
	
	@Autowired
	PopulateWeatheHighLowHelper weatherHighLowHelper;
	
	@Autowired
	PopulateLowHighAvgTempHelper populateLowHighAvgTempHelper;
	
	@Autowired
	YahooWeatherReader yahooWeatherReader;
	
	/**
	 * Delete data from StoreOne (zipCode collection)
	 * @return
	 */
	@RequestMapping(value = "/delete/storeone", method = RequestMethod.GET)
	@ResponseBody
	public String deleteStoreOne() {
		log.info("request deleteStoreOne()");
		zipRepo.removeAll();
		Message msg = new Message();
		msg.setMessage("Deleted successfully!");

		return gson.toJson(msg);
	}

	/**
	 * insert data int StoreOne (zipCode collection) from /data/Texas.csv file.
	 * @return
	 */
	@RequestMapping(value = "/init/storeone", method = RequestMethod.GET)
	@ResponseBody
	public String initStoreOne() throws DataFileNotFoundException, IOException {
		log.info("request initStoreOne()");
		
		Message msg = new Message();
		zipCodeHelper.insertZipCodes();
		msg.setMessage(zipCodeHelper.getRowCount() + " item(s) inserted successfully! ");
		return gson.toJson(msg);
	}

	/**
	 * Delete data from StoreTwo (weatherByZipCode collection)
	 * @return
	 */
	@RequestMapping(value = "/delete/storetwo", method = RequestMethod.GET)
	@ResponseBody
	public String deleteStoreTwo() {
		if(weatherHighLowHelper.isRunning()) {
			weatherHighLowHelper.stop();
		}
		weatherByZipCodeRepository.removeAll();
		Message msg = new Message();
		msg.setMessage("Stroe[2] truncated successfully!");
		return gson.toJson(msg);
	}

	/**
	 * insert data into StoreTwo (weatherByZipCode collection). This method creates a separate thread to populate StoreTwo using Yahoo Old weather API. 
	 * @return
	 */
	@RequestMapping(value = "/init/storetwo", method = RequestMethod.GET)
	@ResponseBody
	public String initStoreTwo() throws JAXBException, IOException {
		weatherHighLowHelper.start();
		Message msg = new Message();
		msg.setMessage("process started for Stroe[2] successfully! Please use [/init/storetwo/status] url for status.");
		return gson.toJson(msg);
	}

	/**
	 * This method returns how many rows has been inserted into weatherByZipCod collection. 
	 * @return
	 * @throws JAXBException
	 * @throws IOException
	 */
	@RequestMapping(value = "/init/storetwo/status", method = RequestMethod.GET)
	@ResponseBody
	public String statusStoreTwo() throws JAXBException, IOException {
		
		StoreStatus msg = new StoreStatus();
		if(weatherHighLowHelper.isRunning()) {
			msg.setRows(weatherHighLowHelper.getRowCount() );
			msg.setProgress(true);
			msg.setStatusMsg(weatherHighLowHelper.getRowCount() + " item(s) populated with low and high temperature");
		}else if(weatherHighLowHelper.isInterrupted()){
			msg.setRows(weatherHighLowHelper.getRowCount() );
			msg.setProgress(false);
			msg.setMessage("Weather service was interrupted");
		}else{
			msg.setRows(weatherHighLowHelper.getRowCount() );
			msg.setProgress(false);
			msg.setMessage("Stroe[2] populated with low and high temperature successfully!( Total: " + weatherHighLowHelper.getRowCount()  + ")");
		}
		return gson.toJson(msg);
	}
	

	/**
	 * Delete data from StoreThree (lowHighAvgTempByZipCode collection)
	 * @return
	 */
	@RequestMapping(value = "/delete/storethree", method = RequestMethod.GET)
	@ResponseBody
	public String deleteStoreThree(){
		lowHighAvgTempByZipCodeRepository.removeAll();
		Message msg = new Message();
		msg.setMessage("Store[3] truncated successfully!");
		return gson.toJson(msg);
			
	}

	/**
	 * Insert data into lowHighAvgTempByZipCode collection using MapReduce. Mapper and Reducer code is under /mapred folder. 
	 * @return
	 */
	@RequestMapping(value = "/init/storethree", method = RequestMethod.GET)
	@ResponseBody
	public String initStoreThree(){
		populateLowHighAvgTempHelper.start();
		StoreStatus msg = new StoreStatus();
		msg.setRows(populateLowHighAvgTempHelper.getRowCount());
		msg.setStatusMsg("Stroe[3] populated with low, high and avg temperature successfully![Using Map Red :)]");
		return gson.toJson(msg);
	}
	
	
	/**
	 * This method return current high, low, lowest, highest and average temperature by Day( Mon, Tue, Wed.....)  
	 * @param zip
	 * @return
	 * @throws JAXBException
	 * @throws IOException
	 * @throws ZipCodeNotFoundException
	 */
	@RequestMapping(value = "/forecast/{zip}", method = RequestMethod.GET)
	@ResponseBody
	public String getForecast(@PathVariable("zip")String zip) throws JAXBException, IOException, ZipCodeNotFoundException{
		Message msg =populateForecastHelper.getForecast(zip);
		
		return gson.toJson(msg);
	}
	
	
	/** Exception handling **/

	@ExceptionHandler(DataFileNotFoundException.class)
	@ResponseBody
	protected String handlerDataFileNotFoundException(DataFileNotFoundException e) {
		Message msg = new Message();
		msg.setCode(1);
		msg.setMessage("Data file not found!");
		return gson.toJson(msg);
	}

	@ExceptionHandler(IOException.class)
	@ResponseBody
	protected String handlerDataFileNotFoundException(IOException e) {
		Message msg = new Message();
		msg.setCode(1);
		msg.setMessage("Data file not found!");
		return gson.toJson(msg);
	}

	@ExceptionHandler(JAXBException.class)
	@ResponseBody
	protected String handlerJAXBException(JAXBException e) {
		Message msg = new Message();
		msg.setCode(1);
		msg.setMessage("Yahoo ServiceUnmarshaller error");
		return gson.toJson(msg);
	}
	
	@ExceptionHandler(MongoException.class)
	@ResponseBody
	protected String handlerMongoException(MongoException e) {
		Message msg = new Message();
		msg.setCode(1);
		msg.setMessage("MapReduce failed horrible!");
		return gson.toJson(msg);
	}
	
	@ExceptionHandler(ZipCodeNotFoundException.class)
	@ResponseBody
	protected String handlerZipCodeNotFoundException(ZipCodeNotFoundException e) {
		Message msg = new Message();
		msg.setCode(1);
		msg.setMessage("Zip code not found!");
		return gson.toJson(msg);
	}
	
	
}
