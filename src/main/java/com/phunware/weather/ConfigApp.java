package com.phunware.weather;

import static com.phunware.weather.AppProperties.DOC_ROOT;
import lombok.Data;
import lombok.extern.log4j.Log4j;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Data
@Log4j
public class ConfigApp {
	private Resource log4jConfig;
	private Resource root;
	private Resource data;
	private Resource mapRedTemplates;
	protected void init() throws Exception {

		try {
			DOC_ROOT = root.getFile().getPath();

			if (DOC_ROOT != null && DOC_ROOT.trim().length() > 0) {
				if (DOC_ROOT.endsWith("\\")) {
					DOC_ROOT = DOC_ROOT.substring(0, DOC_ROOT.length() - 1);
				}
			}
			System.setProperty("DOC_ROOT", DOC_ROOT);
			AppProperties.dataFile = data.getFile().getPath();
			AppProperties.mapRedTemplateFolder=mapRedTemplates.getFile().getPath();
			
			if (log4jConfig != null && log4jConfig.exists()) {
				PropertyConfigurator.configure(log4jConfig.getURL());
			}

			log.info("Done Config");
		} catch (Exception e) {
			log.fatal("Error at loading config", e);
		}

	}

}
