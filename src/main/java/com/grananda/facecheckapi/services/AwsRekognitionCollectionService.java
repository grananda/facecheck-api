package com.grananda.facecheckapi.services;

import software.amazon.awssdk.services.rekognition.model.CreateCollectionResponse;
import software.amazon.awssdk.services.rekognition.model.DeleteCollectionResponse;
import software.amazon.awssdk.services.rekognition.model.DescribeCollectionResponse;
import software.amazon.awssdk.services.rekognition.model.ListCollectionsResponse;

public interface AwsRekognitionCollectionService {

    int AWS_SUCCESS_STATUS_CODE = 200;

    CreateCollectionResponse createFaceMemoryCollection(String collectionId);

    DescribeCollectionResponse describeFaceMemoryCollection(String collectionId);

    ListCollectionsResponse listFaceMemoryCollections();

    DeleteCollectionResponse deleteFaceMemoryCollection(String collectionId);
}
