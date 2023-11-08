package com.oracle.fn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.aivision.AIServiceVisionClient;
import com.oracle.bmc.aivision.model.AnalyzeImageDetails;
import com.oracle.bmc.aivision.model.AnalyzeImageResult;
import com.oracle.bmc.aivision.model.CreateImageJobDetails;
import com.oracle.bmc.aivision.model.ImageClassificationFeature;
import com.oracle.bmc.aivision.model.ImageObjectDetectionFeature;
import com.oracle.bmc.aivision.model.ObjectListInlineInputLocation;
import com.oracle.bmc.aivision.model.ObjectLocation;
import com.oracle.bmc.aivision.model.ObjectStorageImageDetails;
import com.oracle.bmc.aivision.model.OutputLocation;
import com.oracle.bmc.aivision.requests.AnalyzeImageRequest;
import com.oracle.bmc.aivision.requests.CreateImageJobRequest;
import com.oracle.bmc.aivision.responses.AnalyzeImageResponse;
import com.oracle.bmc.aivision.responses.CreateImageJobResponse;
import com.oracle.bmc.auth.AbstractAuthenticationDetailsProvider;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.auth.ResourcePrincipalAuthenticationDetailsProvider;

@Component
public class OciSdkHelper {

	public AuthenticationDetailsProvider getAuthenticationDetailsProvider(String configurationFilePath, String profile)
			throws IOException {
		final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(configurationFilePath, profile);
		return new ConfigFileAuthenticationDetailsProvider(configFile);
	}

	public ResourcePrincipalAuthenticationDetailsProvider getResourcePrincipalAuthenticationDetailsProvider() {
		return ResourcePrincipalAuthenticationDetailsProvider.builder().build();
	}

	public AnalyzeImageResult getAnalyzeImageResult(AbstractAuthenticationDetailsProvider p, String compartmentId,
			String namespaceName, String bucketName, String fileName) {

		AIServiceVisionClient client = AIServiceVisionClient.builder().build(p);

		AnalyzeImageDetails details = AnalyzeImageDetails.builder().compartmentId(compartmentId)
				.features(List.of(ImageClassificationFeature.builder().build())).image(ObjectStorageImageDetails
						.builder().namespaceName(namespaceName).bucketName(bucketName).objectName(fileName).build())
				.build();

		AnalyzeImageRequest analyzeImageRequest = AnalyzeImageRequest.builder().analyzeImageDetails(details).build();

		AnalyzeImageResponse analyzeResponse = client.analyzeImage(analyzeImageRequest);

		return analyzeResponse.getAnalyzeImageResult();

	}

	public int putCreateImageJobDetailsToObjectStorage(AbstractAuthenticationDetailsProvider p, String namespaceName,
			String bucketName, String fileName, String prefix) {

		AIServiceVisionClient client = AIServiceVisionClient.builder().build(p);

		CreateImageJobDetails createImageJobDetails = CreateImageJobDetails.builder()
				.inputLocation(ObjectListInlineInputLocation.builder()
						.objectLocations(new ArrayList<>(Arrays.asList(ObjectLocation.builder()
								.namespaceName(namespaceName).bucketName(bucketName).objectName(fileName).build())))
						.build())
				.features(new ArrayList<>(Arrays.asList(ImageObjectDetectionFeature.builder().maxResults(989).build())))
				.outputLocation(OutputLocation.builder().namespaceName(namespaceName).bucketName(bucketName)
						.prefix(prefix).build())
				.build();

		CreateImageJobRequest createImageJobRequest = CreateImageJobRequest.builder()
				.createImageJobDetails(createImageJobDetails).build();
		
		CreateImageJobResponse createImageJobResponse = client.createImageJob(createImageJobRequest);
		
		return createImageJobResponse.get__httpStatusCode__();
	
	}

}
