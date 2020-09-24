package com.grananda.facecheckapi.services;

import com.grananda.facecheckapi.domain.FaceMemory;
import com.grananda.facecheckapi.domain.FaceMemoryCollection;
import com.grananda.facecheckapi.domain.User;
import com.grananda.facecheckapi.repositories.FaceMemoryRepository;
import com.grananda.facecheckapi.utils.FaceMemoryCollectionFactory;
import com.grananda.facecheckapi.utils.FaceMemoryFactory;
import com.grananda.facecheckapi.utils.ImageFactory;
import com.grananda.facecheckapi.utils.UserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import software.amazon.awssdk.services.rekognition.model.*;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@SpringBootTest
class FaceMemoryServiceImplTest {

    @MockBean
    AwsRekognitionFaceService awsRekognitionFaceService;

    @MockBean
    FaceMemoryRepository faceMemoryRepository;

    @Autowired
    FaceMemoryServiceImpl faceMemoryService;

    @Test
    void a_face_is_stored_in_a_collection() throws IOException {
        // Given
        FaceMemoryCollection faceMemoryCollection = FaceMemoryCollectionFactory.create();

        User user = UserFactory.create();

        Image image = ImageFactory.create("assets/image1a.jpg");

        String faceId = UUID.randomUUID().toString();

        Face face = Face.builder()
                .faceId(faceId)
                .build();

        FaceRecord faceRecord = FaceRecord.builder()
                .face(face)
                .build();

        IndexFacesResponse indexFacesResponse = IndexFacesResponse.builder()
                .faceRecords(faceRecord)
                .build();

        when(awsRekognitionFaceService.indexFace(faceMemoryCollection.getCollectionId(), image))
                .thenReturn(indexFacesResponse);

        // When
        FaceMemory faceMemory = faceMemoryService.storeFacialMemory(user, faceMemoryCollection, image);

        // Then
        assertEquals(faceMemory.getCollection().getCollectionId(), faceMemoryCollection.getCollectionId());
        assertEquals(faceMemory.getFaceId(), faceId);
        assertEquals(faceMemory.getUser().getEmail(), user.getEmail());
    }

    @Test
    void a_face_is_removed_from_a_collection() {
        // Given
        FaceMemoryCollection faceMemoryCollection = FaceMemoryCollectionFactory.create();
        FaceMemory faceMemory = FaceMemoryFactory.create();

        faceMemory.setCollection(faceMemoryCollection);
        faceMemoryCollection.getFaces().add(faceMemory);

        DeleteFacesResponse deleteFacesResponse = DeleteFacesResponse.builder()
                .deletedFaces(faceMemory.getFaceId())
                .build();

        when(awsRekognitionFaceService.forgetFace(faceMemory.getFaceId(), faceMemoryCollection.getCollectionId()))
                .thenReturn(deleteFacesResponse);

        // When
        boolean response = faceMemoryService.removeFacialMemory(faceMemory);

        // Then
        assertTrue(response);
    }
}