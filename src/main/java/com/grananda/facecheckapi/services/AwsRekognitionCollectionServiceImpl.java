package com.grananda.facecheckapi.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

@Log4j2
@Service
public class AwsRekognitionCollectionServiceImpl implements AwsRekognitionCollectionService {

    private final RekognitionClient client;

    public AwsRekognitionCollectionServiceImpl(RekognitionClient client) {
        this.client = client;
    }

    @Override
    public CreateCollectionResponse createFaceMemoryCollection(String collectionId) {
        CreateCollectionResponse response = CreateCollectionResponse.builder().build();

        try {
            CreateCollectionRequest request = CreateCollectionRequest.builder()
                    .collectionId(collectionId)
                    .build();

            response = client.createCollection(request);
        } catch (Exception awsException) {
            log.error("AwsSdkError:AwsRekognitionCollectionServiceImpl:CreateCollectionResponse: " + awsException.toString());
        }

        return response;
    }

    @Override
    public DescribeCollectionResponse describeFaceMemoryCollection(String collectionId) {
        DescribeCollectionResponse response = DescribeCollectionResponse.builder().build();

        try {
            DescribeCollectionRequest request = DescribeCollectionRequest.builder()
                    .collectionId(collectionId)
                    .build();

            response = client.describeCollection(request);
        } catch (Exception awsException) {
            log.error("AwsSdkError:AwsRekognitionCollectionServiceImpl:DescribeCollectionResponse: " + awsException.toString());
        }

        return response;
    }

    @Override
    public ListCollectionsResponse listFaceMemoryCollections() {
        ListCollectionsResponse response = ListCollectionsResponse.builder().build();

        try {
            ListCollectionsRequest request = ListCollectionsRequest.builder().build();

            response = client.listCollections(request);
        } catch (Exception awsException) {
            log.error("AwsSdkError:AwsRekognitionCollectionServiceImpl:ListCollectionsResponse: " + awsException.toString());
        }

        return response;
    }

    @Override
    public DeleteCollectionResponse deleteFaceMemoryCollection(String collectionId) {
        DeleteCollectionResponse response = DeleteCollectionResponse.builder().build();

        try {
            DeleteCollectionRequest request = DeleteCollectionRequest.builder()
                    .collectionId(collectionId)
                    .build();

            response = client.deleteCollection(request);
        } catch (Exception awsException) {
            log.error("AwsSdkError:AwsRekognitionCollectionServiceImpl:DeleteCollectionResponse: " + awsException.toString());
        }

        return response;
    }
}
