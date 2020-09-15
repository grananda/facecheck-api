package com.grananda.facecheckapi.services;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

@Service
public class AwsRekognitionCollectionServiceImpl implements AwsRekognitionCollectionService {

    private final RekognitionClient client;

    public AwsRekognitionCollectionServiceImpl(RekognitionClient client) {
        this.client = client;
    }

    @Override
    public CreateCollectionResponse createFaceMemoryCollection(String collectionId) {
        CreateCollectionRequest request = CreateCollectionRequest.builder()
                .collectionId(collectionId)
                .build();

        return client.createCollection(request);
    }

    @Override
    public DescribeCollectionResponse describeFaceMemoryCollection(String collectionId) {
        DescribeCollectionRequest request = DescribeCollectionRequest.builder()
                .collectionId(collectionId)
                .build();

        return client.describeCollection(request);
    }

    @Override
    public ListCollectionsResponse listFaceMemoryCollections() {
        ListCollectionsRequest request = ListCollectionsRequest.builder().build();

        return client.listCollections(request);
    }

    @Override
    public DeleteCollectionResponse deleteFaceMemoryCollection(String collectionId) {
        DeleteCollectionRequest request = DeleteCollectionRequest.builder()
                .collectionId(collectionId)
                .build();

        return client.deleteCollection(request);
    }
}
