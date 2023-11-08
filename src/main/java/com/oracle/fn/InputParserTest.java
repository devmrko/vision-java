package com.oracle.fn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.oracle.fn")
public class InputParserTest {
	
	private static final Logger logger = LoggerFactory.getLogger(InputParserTest.class);
	
	public static void main(String[] args) {
		
		String input = "{\"eventType\":\"com.oraclecloud.objectstorage.createobject\",\"cloudEventsVersion\":\"0.1\",\"eventTypeVersion\":\"2.0\",\"source\":\"ObjectStorage\",\"eventTime\":\"2023-11-06T14:39:49Z\",\"contentType\":\"application/json\",\"data\":{\"compartmentId\":\"ocid1.tenancy.oc1..aaaaaaaa2wpfjruuzputbhuzhz2cbwnizjgdyg5q2xqf6nlzm66wayav7a2a\",\"compartmentName\":\"joungminkoaws\",\"resourceName\":\"PXL_20230704_015632626.jpg\",\"resourceId\":\"/n/idyhsdamac8c/b/test/o/PXL_20230704_015632626.jpg\",\"availabilityDomain\":\"IAD-AD-2\",\"additionalDetails\":{\"bucketName\":\"test\",\"versionId\":\"6d5fa574-713a-40f0-ab4c-517d3cca8afe\",\"archivalState\":\"Available\",\"namespace\":\"idyhsdamac8c\",\"bucketId\":\"ocid1.bucket.oc1.iad.aaaaaaaapiej32u3nbo7qgpm6uikkq3phbonb3wq6ugyrmm7l5xasaijscmq\",\"eTag\":\"51ce4324-653e-4dcf-ab3e-5ec1bd4dbd9f\"}},\"eventID\":\"3291a385-fc5b-dda4-ac2a-6eadff71d112\",\"extensions\":{\"compartmentId\":\"ocid1.tenancy.oc1..aaaaaaaa2wpfjruuzputbhuzhz2cbwnizjgdyg5q2xqf6nlzm66wayav7a2a\"}}";

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(InputParserTest.class);

		InputParser inputParser = context.getBean(InputParser.class);
		
		try {
			String result = inputParser.getParsingValue(input);
			logger.info("getParsingValue: {}", result);
			
		} catch (InputParseException e) {
			e.printStackTrace();
			logger.error("{})", e.getMessage());
		}
		
		context.close();
		
	}
	
}
