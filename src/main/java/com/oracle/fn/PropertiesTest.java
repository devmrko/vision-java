package com.oracle.fn;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

@Configuration
@ComponentScan(basePackages = "com.oracle.fn")
public class PropertiesTest {
	
	private static final Logger logger = LoggerFactory.getLogger(PropertiesTest.class);
	
	public static void main(String[] args) {
		
		Resource resource = new ClassPathResource("application.properties");
        Properties properties;
		try {
			properties = PropertiesLoaderUtils.loadProperties(resource);
			logger.info("environment.variable from application.properties: {}", properties.getProperty("environment.variable"));
			logger.info("environment.variable from system environment: {}", System.getenv("ENV_VAR"));
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("{})", e.getMessage());
		}
		
	}
	
}
