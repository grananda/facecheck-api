package com.grananda.facecheckapi.services;

import software.amazon.awssdk.services.rekognition.model.CreateCollectionResponse;
import software.amazon.awssdk.services.rekognition.model.DeleteCollectionResponse;
import software.amazon.awssdk.services.rekognition.model.DescribeCollectionResponse;
import software.amazon.awssdk.services.rekognition.model.ListCollectionsResponse;

public interface AwsRekognitionCollectionService {

    CreateCollectionResponse createFaceMemoryCollection(String collectionId);

    DescribeCollectionResponse describeFaceMemoryCollection(String collectionId);

    ListCollectionsResponse listFaceMemoryCollections();

    DeleteCollectionResponse deleteFaceMemoryCollection(String collectionId);
}
