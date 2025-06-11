package com.app.distribution.movie_biz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;
import software.amazon.awssdk.services.sts.model.Credentials;

@Configuration
public class AwsConfig {
	
	public static final String roleARN = "arn:aws:iam::326113948308:role/mtb-task-exec-role";
	public static final String roleSessionName = "mtb-session";

	@Bean
	Credentials credentials() {
		AssumeRoleRequest roleRequest = AssumeRoleRequest.builder().roleArn(roleARN).roleSessionName(roleSessionName)
				.build();
		StsClient stsClient = StsClient.builder().build();
		AssumeRoleResponse roleResponse = stsClient.assumeRole(roleRequest);
		Credentials awsBasicCredentials = roleResponse.credentials();
		return awsBasicCredentials;
	}

	@Bean
	S3Client s3Client() {
		return S3Client.builder().build();
	}
}
