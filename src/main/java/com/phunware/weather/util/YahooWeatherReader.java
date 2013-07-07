package com.phunware.weather.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import lombok.extern.log4j.Log4j;

import com.phunware.weather.AppProperties;
import com.phunware.weather.rss.Rss;

@Log4j
public class YahooWeatherReader {

	public static final String ZIP_PARAMETER_NAME = "p";
	protected static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	protected Unmarshaller unmarshaller;

	public YahooWeatherReader() {
		try {
			JAXBContext context = JAXBContext.newInstance(Rss.class);
			unmarshaller = context.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public Rss parse(String xml) throws JAXBException {
		return (Rss) unmarshaller.unmarshal(new StringReader(xml));
	}

	public Rss getForecast(String zipCode) throws JAXBException, IOException {
		log.info("forecast for zipCode " + zipCode);
		String url = createURL(zipCode);
		log.info("url: [" + url + "]");
		String xml = retrieveRSS(url);
		return parse(xml);
	}

	protected String createURL(String zipCode) {
		StringBuilder url = new StringBuilder(AppProperties.YAHOO_WEATHER_URL);
		url.append('?');
		url.append(ZIP_PARAMETER_NAME);
		url.append('=');
		url.append(zipCode);

		return url.toString();
	}

	protected String retrieveRSS(String serviceUrl) throws IOException {
		URL url = new URL(serviceUrl);
		URLConnection connection = url.openConnection();
		InputStream is = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(is);
		StringWriter writer = new StringWriter();
		copy(reader, writer);
		reader.close();
		is.close();

		return writer.toString();
	}

	public static long copy(Reader input, Writer output) throws IOException {
		char[] buffer = new char[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}
}
