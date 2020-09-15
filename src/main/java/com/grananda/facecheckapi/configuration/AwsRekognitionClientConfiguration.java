package com.grananda.facecheckapi.configuration;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.rekognition.RekognitionClient;

import java.util.Optional;

@Configuration
public class AwsRekognitionClientConfiguration {

    /**
     * @return RekognitionClient
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public RekognitionClient rekognitionClient(Optional<String> profile) {

        return RekognitionClient.builder()
                .credentialsProvider(generateProfileCredentialsProvider(profile))
                .build();
    }

    /**
     * @return ProfileCredentialsProvider
     */
    private ProfileCredentialsProvider generateProfileCredentialsProvider(Optional<String> profile) {

        return ProfileCredentialsProvider.builder()
                .profileName(profile.orElse("default"))
                .build();
    }
}
