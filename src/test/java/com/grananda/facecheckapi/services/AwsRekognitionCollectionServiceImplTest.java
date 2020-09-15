package com.grananda.facecheckapi.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.rekognition.model.CreateCollectionResponse;
import software.amazon.awssdk.services.rekognition.model.DeleteCollectionResponse;
import software.amazon.awssdk.services.rekognition.model.DescribeCollectionResponse;
import software.amazon.awssdk.services.rekognition.model.ListCollectionsResponse;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AwsRekognitionCollectionServiceImplTest {

    @Autowired
    AwsRekognitionCollectionService rekognitionCollectionService;

    @Test
    void createFaceMemoryCollection() {
        // Given
        String collectionId = UUID.randomUUID().toString();

        // When
        CreateCollectionResponse response = rekognitionCollectionService.createFaceMemoryCollection(collectionId);

        // Then
        assertEquals(response.statusCode().toString(), "200");
        assertEquals(response.collectionArn().toString().split("/")[1], collectionId);
    }

    @Test
    void describeFaceMemoryCollection() {
        // Given
        String collectionId = UUID.randomUUID().toString();

        rekognitionCollectionService.createFaceMemoryCollection(collectionId);

        // When
        DescribeCollectionResponse response = rekognitionCollectionService.describeFaceMemoryCollection(collectionId);

        // Then
        assertEquals(response.collectionARN().toString().split("/")[1], collectionId);
    }

    @Test
    void listMemoryCollections() {
        // Given
        String collectionId = UUID.randomUUID().toString();

        rekognitionCollectionService.createFaceMemoryCollection(collectionId);

        // When
        ListCollectionsResponse response = rekognitionCollectionService.listFaceMemoryCollections();

        // Then
        assertTrue(response.collectionIds().stream().anyMatch(item -> item.equals(collectionId)));
    }

    @Test
    void deleteMemoryCollection() {
        // Given
        String collectionId = UUID.randomUUID().toString();

        rekognitionCollectionService.createFaceMemoryCollection(collectionId);

        // When
        DeleteCollectionResponse response = rekognitionCollectionService.deleteFaceMemoryCollection(collectionId);

        // Then
        assertEquals(response.statusCode().toString(), "200");
    }

    @AfterEach
    private void cleanUp() {
        ListCollectionsResponse response = rekognitionCollectionService.listFaceMemoryCollections();

        for (String collectionId : response.collectionIds()) {
            System.out.println("DELETING COLLECTION: "+collectionId);
            rekognitionCollectionService.deleteFaceMemoryCollection(collectionId);
        }
    }
}