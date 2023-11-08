package com.oracle.fn;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.google.gson.Gson;
import com.oracle.bmc.aivision.model.AnalyzeImageResult;
import com.oracle.bmc.auth.ResourcePrincipalAuthenticationDetailsProvider;

@Configuration
@ComponentScan(basePackages = "com.oracle.fn")
public class VisionFunction {

	private static final Logger logger = LoggerFactory.getLogger(VisionFunction.class);

	public String handleRequest(String input) {

		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(VisionFunction.class)) {

			InputParser inputParser = context.getBean(InputParser.class);
			OciSdkHelper ociSdkHelper = context.getBean(OciSdkHelper.class);
			RestAPIHelper restAPIHelper = context.getBean(RestAPIHelper.class);

			Resource resource = new ClassPathResource("application.properties");
			Properties properties;
			properties = PropertiesLoaderUtils.loadProperties(resource);

			String namespaceName = System.getenv("namespaceName") == null ? properties.getProperty("namespaceName") : System.getenv("namespaceName");
			String bucketName = System.getenv("bucketName") == null ? properties.getProperty("bucketName") : System.getenv("bucketName");
			String compartmentId = System.getenv("compartmentId") == null ? properties.getProperty("compartmentId") : System.getenv("compartmentId");
			String urlStr = System.getenv("vision.post.api.url") == null ? properties.getProperty("vision.post.api.url") : System.getenv("vision.post.api.url");

			AnalyzeImageResult analyzeImageResult;
			try {
				String fileName;
				fileName = inputParser.getParsingValue(input);
				logger.debug("fileName: {}", fileName);

				ResourcePrincipalAuthenticationDetailsProvider p = ociSdkHelper.getResourcePrincipalAuthenticationDetailsProvider();
				analyzeImageResult = ociSdkHelper.getAnalyzeImageResult(p, compartmentId, namespaceName, bucketName, fileName);
				Gson gs = new Gson();
				String result = gs.toJson(analyzeImageResult.getLabels());
				logger.info("{}", result);
				
				
				URI uri = new URI(urlStr);
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.add("FILE_NM", fileName);
				headers.add("REQUEST_ID", fileName.split("___")[0]);
				restAPIHelper.callPostForObject(uri, headers, result);
				
				return result;
		        
			} catch (InputParseException e) {
				e.printStackTrace();
				logger.error("InputParseException error: {}", e.getMessage());
				return "Input parameter parse error";
			} catch (URISyntaxException e) {
				e.printStackTrace();
				logger.error("URISyntaxException error: {}", e.getMessage());
				return "URISyntaxException error";
			}

		} catch (IOException e) {
			e.printStackTrace();
			logger.error("PropertiesLoaderUtils error: {}", e.getMessage());
			return "properties variables load error";
			
		}
		
	}

}
