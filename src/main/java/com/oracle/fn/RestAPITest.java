package com.oracle.fn;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Configuration
@ComponentScan(basePackages = "com.oracle.fn")
public class RestAPITest {

	private static final Logger logger = LoggerFactory.getLogger(RestAPITest.class);

	public static void main(String[] args) throws IOException {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(VisionTest.class)) {

			RestAPIHelper restAPIHelper = context.getBean(RestAPIHelper.class);

			Resource resource = new ClassPathResource("application.properties");
			Properties properties;
			properties = PropertiesLoaderUtils.loadProperties(resource);

			String urlStr = properties.getProperty("vision.post.api.url");
			logger.info("URL: {}", urlStr);
			String fileName = "";
			
			String visionResult = "[{\"name\":\"Metal\",\"confidence\":0.9920825,\"__explicitlySet__\":[\"confidence\",\"name\"]},{\"name\":\"Tin can\",\"confidence\":0.9888545,\"__explicitlySet__\":[\"confidence\",\"name\"]},{\"name\":\"Drinkware\",\"confidence\":0.9646649,\"__explicitlySet__\":[\"confidence\",\"name\"]},{\"name\":\"Cup\",\"confidence\":0.9502804,\"__explicitlySet__\":[\"confidence\",\"name\"]},{\"name\":\"Table\",\"confidence\":0.93937266,\"__explicitlySet__\":[\"confidence\",\"name\"]}]\n"
					+ "";

//			URL url = new URL(urlStr);
//			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//			httpURLConnection.setRequestMethod("POST");
//			httpURLConnection.setRequestProperty("Content-Type", "application/json");
//			httpURLConnection.setRequestProperty("REQUEST_ID", fileName.split("___")[0]);
//			httpURLConnection.setRequestProperty("FILE_NM", fileName);
//			httpURLConnection.setDoOutput(true);
//			
//			int responseCode = restAPIHelper.callPostAPI(url, httpURLConnection, visionResult);
//			logger.info("responseCode: {}", responseCode);

			URI uri = new URI(urlStr);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("FILE_NM", fileName);
			headers.add("REQUEST_ID", fileName.split("___")[0]);
			restAPIHelper.callPostForObject(uri, headers, visionResult);

		} catch (BeansException e) {
			e.printStackTrace();
			logger.error("BeansException: {}", e.getMessage());

		} catch (URISyntaxException e) {
			e.printStackTrace();
			logger.error("URISyntaxException: {}", e.getMessage());
		}
	}

}
