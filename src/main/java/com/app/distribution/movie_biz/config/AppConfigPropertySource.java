package com.app.distribution.movie_biz.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;

import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.appconfigdata.AppConfigDataClient;
import software.amazon.awssdk.services.appconfigdata.model.GetLatestConfigurationRequest;
import software.amazon.awssdk.services.appconfigdata.model.GetLatestConfigurationResponse;
import software.amazon.awssdk.services.appconfigdata.model.StartConfigurationSessionRequest;
import software.amazon.awssdk.services.sts.model.Credentials;

@Configuration
public class AppConfigPropertySource {
	private Properties properties;
	@Autowired
	AwsConfig awsAppConfig;
	
	public Object[] getPropertyNames() {
		Object[] names = this.properties.keySet().toArray();
		return names;
	}

	public Object getProperty(String name) {
		return this.properties.get(name);
	}

	public void init() {
		Credentials awsBasicCredentials = awsAppConfig.credentials();
		AppConfigDataClient client = AppConfigDataClient.builder()
				.credentialsProvider(
						StaticCredentialsProvider.create(AwsSessionCredentials.create(awsBasicCredentials.accessKeyId(),
								awsBasicCredentials.secretAccessKey(), awsBasicCredentials.sessionToken())))
				.build();
		StartConfigurationSessionRequest startConfigurationSessionRequest = StartConfigurationSessionRequest.builder()
				.applicationIdentifier("movietradeapp").environmentIdentifier("dev")
				.configurationProfileIdentifier("mtbconfig").build();
		String sessionToken = client.startConfigurationSession(startConfigurationSessionRequest)
				.initialConfigurationToken();
		GetLatestConfigurationRequest latestConfigurationRequest = GetLatestConfigurationRequest.builder()
				.configurationToken(sessionToken).build();
		GetLatestConfigurationResponse latestConfigurationResponse = client
				.getLatestConfiguration(latestConfigurationRequest);
		byte[] buffer = latestConfigurationResponse.configuration().asByteArray();
		processYamlContent(buffer);
	}
	
	private void processYamlContent(byte[] byteBuffer) {	
		YamlPropertiesFactoryBean bean = new YamlPropertiesFactoryBean();

		bean.setResources(new ByteArrayResource(byteBuffer));
		
		this.properties = bean.getObject();
	}
}
