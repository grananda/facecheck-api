package com.grananda.facecheckapi.services;

import com.github.javafaker.Faker;
import com.grananda.facecheckapi.domain.FaceMemoryCollection;
import com.grananda.facecheckapi.domain.Organization;
import com.grananda.facecheckapi.repositories.FaceMemoryCollectionRepository;
import com.grananda.facecheckapi.repositories.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import software.amazon.awssdk.services.rekognition.model.CreateCollectionResponse;
import software.amazon.awssdk.services.rekognition.model.DeleteCollectionResponse;

import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class FaceMemoryCollectionServiceImplTest {

    @MockBean
    AwsRekognitionCollectionService awsRekognitionCollectionService;

    @MockBean
    UuIdGeneratorService uuIdGeneratorService;

    @MockBean
    FaceMemoryCollectionRepository faceMemoryCollectionRepository;

    @Autowired
    FaceMemoryCollectionServiceImpl service;

    @Autowired
    OrganizationRepository organizationRepository;

    @Test
    void a_face_memory_collection_is_registered() {
        // Given
        UUID collectionId = UUID.randomUUID();
        String collectionArn = Faker.instance().lorem().word();
        String collectionName = Faker.instance().lorem().word();

        Organization organization = Organization.builder()
                .memoryCollections(new HashSet<>())
                .name(Faker.instance().company().name())
                .build();

        organizationRepository.save(organization);

        CreateCollectionResponse createCollectionResponse = CreateCollectionResponse.builder()
                .collectionArn(collectionArn)
                .statusCode(200)
                .build();

        FaceMemoryCollection faceMemoryCollection = FaceMemoryCollection.builder()
                .name(collectionName)
                .collectionId(collectionId.toString())
                .collectionArn(createCollectionResponse.collectionArn())
                .build();

        when(uuIdGeneratorService.generateUuId())
                .thenReturn(collectionId);

        when(awsRekognitionCollectionService.createFaceMemoryCollection(anyString()))
                .thenReturn(createCollectionResponse);

        when(faceMemoryCollectionRepository.save(any(FaceMemoryCollection.class)))
                .thenReturn(faceMemoryCollection);

        // When
        FaceMemoryCollection collection = service.registerFaceMemoryCollection(organization, collectionName);

        // Then
        assertEquals(collectionArn, collection.getCollectionArn());
        assertEquals(collectionId.toString(), collection.getCollectionId());
        assertEquals(collectionName, collection.getName());
        assertEquals(1, organization.getMemoryCollections().size());

        verify(faceMemoryCollectionRepository, times(1)).save(any(FaceMemoryCollection.class));
    }

    @Test
    void a_face_memory_collection_is_removed() {
        // Given
        UUID collectionId = UUID.randomUUID();
        String collectionArn = Faker.instance().lorem().word();
        String collectionName = Faker.instance().lorem().word();

        DeleteCollectionResponse deleteCollectionResponse = DeleteCollectionResponse.builder()
                .statusCode(200)
                .build();

        when(awsRekognitionCollectionService.deleteFaceMemoryCollection(anyString()))
                .thenReturn(deleteCollectionResponse);

        FaceMemoryCollection faceMemoryCollection = FaceMemoryCollection.builder()
                .collectionArn(collectionArn)
                .name(collectionName)
                .collectionId(collectionId.toString())
                .build();

        faceMemoryCollectionRepository.save(faceMemoryCollection);

        // When
        Boolean response = service.removeFaceMemoryCollection(faceMemoryCollection);

        // Then
        assertTrue(response);
        verify(faceMemoryCollectionRepository, times(1)).delete(any(FaceMemoryCollection.class));
    }
}