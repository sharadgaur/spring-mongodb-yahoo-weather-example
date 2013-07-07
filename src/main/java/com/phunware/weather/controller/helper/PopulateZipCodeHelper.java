package com.phunware.weather.controller.helper;

import java.io.IOException;

import lombok.extern.log4j.Log4j;

import com.phunware.weather.AppProperties;
import com.phunware.weather.db.domain.ZipCode;
import com.phunware.weather.db.repositories.ZipCodeRepository;
import com.phunware.weather.exceptions.DataFileNotFoundException;
import com.phunware.weather.util.CSVListener;
import com.phunware.weather.util.CSVReader;


@Log4j
public class PopulateZipCodeHelper implements CSVListener {
	private int rowCount = 0;

	private ZipCodeRepository zipRepo;


	public PopulateZipCodeHelper( ZipCodeRepository zipRepo) {
		log.info("init PopulateZipCodeHelper()");
		this.zipRepo = zipRepo;
	}

	public void insertZipCodes() throws DataFileNotFoundException, IOException {
		log.info("starting PopulateZipCodeHelper().insertZipCodes()");
		rowCount = 0;
		CSVReader reader = new CSVReader(AppProperties.dataFile, this);
		reader.start();
	}

	public void onRow(String[] data) {
		if (data != null && data.length >= 2) {
			ZipCode zip = new ZipCode();
			zip.setCiti(data[0]);
			zip.setZip(data[1]);
			zipRepo.save(zip);
			rowCount++;
		}

	}

	public int getRowCount() {
		return rowCount;
	}
}
