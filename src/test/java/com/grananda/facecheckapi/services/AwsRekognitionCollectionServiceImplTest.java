package com.grananda.facecheckapi.services;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.rekognition.model.CreateCollectionResponse;
import software.amazon.awssdk.services.rekognition.model.DeleteCollectionResponse;
import software.amazon.awssdk.services.rekognition.model.DescribeCollectionResponse;
import software.amazon.awssdk.services.rekognition.model.ListCollectionsResponse;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled
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
}