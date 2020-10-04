package com.grananda.facecheckapi.services;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.rekognition.model.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AwsRekognitionCollectionServiceImplTest extends BaseAwsRekognitionTest {

    @Test
    void a_collection_is_created() {
        // Given
        String collectionId = UUID.randomUUID().toString();

        // When
        CreateCollectionResponse response = rekognitionCollectionService.createFaceMemoryCollection(collectionId);

        // Then
        assertEquals(response.statusCode().toString(), "200");
        assertEquals(response.collectionArn().toString().split("/")[1], collectionId);
    }

    @Test
    void a_collection_is_described() {
        // Given
        String collectionId = UUID.randomUUID().toString();

        rekognitionCollectionService.createFaceMemoryCollection(collectionId);

        // When
        DescribeCollectionResponse response = rekognitionCollectionService.describeFaceMemoryCollection(collectionId);

        // Then
        assertEquals(response.collectionARN().toString().split("/")[1], collectionId);
    }

    @Test
    void a_non_existing_collection_can_not_be_described() {
        // Given
        String collectionId = UUID.randomUUID().toString();

        rekognitionCollectionService.createFaceMemoryCollection(collectionId);

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            rekognitionCollectionService.describeFaceMemoryCollection(UUID.randomUUID().toString());
        });

        // Then
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void a_list_of_collection_is_requested() {
        // Given
        String collectionId = UUID.randomUUID().toString();

        rekognitionCollectionService.createFaceMemoryCollection(collectionId);

        // When
        ListCollectionsResponse response = rekognitionCollectionService.listFaceMemoryCollections();

        // Then
        assertTrue(response.collectionIds().stream().anyMatch(item -> item.equals(collectionId)));
    }


    @Test
    void a_collection_is_removed() {
        // Given
        String collectionId = UUID.randomUUID().toString();

        rekognitionCollectionService.createFaceMemoryCollection(collectionId);

        // When
        DeleteCollectionResponse response = rekognitionCollectionService.deleteFaceMemoryCollection(collectionId);

        // Then
        assertEquals(response.statusCode().toString(), "200");
    }

    @Test
    void a_non_existing_collection_can_not_be_removed() {
        // Given
        String collectionId = UUID.randomUUID().toString();

        rekognitionCollectionService.createFaceMemoryCollection(collectionId);

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            rekognitionCollectionService.deleteFaceMemoryCollection(UUID.randomUUID().toString());
        });

        // Then
        assertTrue(exception.getMessage().contains("not exist"));
    }
}