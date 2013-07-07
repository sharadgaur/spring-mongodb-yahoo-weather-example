package com.phunware.weather.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
//import java.nio.file.FileSystems;
//import java.nio.file.Files;
//import java.nio.file.Path;

import lombok.Data;
import lombok.extern.log4j.Log4j;

import com.phunware.weather.exceptions.DataFileNotFoundException;

@Log4j
@Data
public class CSVReader {
	private CSVListener listener;
	private File file;

	public CSVReader(String path, CSVListener listener) throws DataFileNotFoundException {
		File file = new File(path);
		if (file != null && file.exists()) {
			this.listener = listener;
			this.file = file;
		} else {
			log.error("State zip code resource file not found!");
			throw new DataFileNotFoundException("File name is  :" + file);
		}
	}

	public void start() throws IOException {
		if (getListener() != null) {
//			Charset charset = Charset.forName("US-ASCII");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
//			Path path = FileSystems.getDefault().getPath(file.getPath());

//			BufferedReader reader = Files.newBufferedReader(path, charset);
			String line = null;
			while ((line = reader.readLine()) != null) {
				getListener().onRow(line.trim().split(","));
			}

		}
	}
}
