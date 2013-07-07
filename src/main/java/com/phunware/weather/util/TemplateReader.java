package com.phunware.weather.util;

import java.util.HashMap;
import java.util.Iterator;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import com.phunware.weather.AppProperties;
import com.phunware.weather.exceptions.TemplateNotFoundException;


public class TemplateReader {
	private  StringTemplateGroup templateGroup = null;

	private void init(){
		templateGroup = new StringTemplateGroup(
				"mapred",AppProperties.mapRedTemplateFolder);
		templateGroup.setRefreshInterval(1000); 
	}

	public String createMapper( HashMap<String, Object> map) {
		return createHtml(AppProperties.MAPPER_FILE_NAME, map);
	}
	
	
	public String createReducer( HashMap<String, Object> map) {
		return createHtml(AppProperties.REDUCER_FILE_NAME, map);
	}
	
	/**
	 * 
	 * @param templateName
	 * @param map
	 * @return
	 */
	protected String createHtml(String templateName, HashMap<String, Object> map) {
		if(templateGroup==null) {
			init();
		}
		StringTemplate page = templateGroup.getInstanceOf(templateName);
		if (page == null) {
			throw new TemplateNotFoundException();
		}
		if(map!=null) {
			Iterator<String> keys =map.keySet().iterator();
			String key="";
			while(keys.hasNext()) {
				key = keys.next();
				page.setAttribute(key, map.get(key));
			}
		}
		
		return page.toString();
	}
}
