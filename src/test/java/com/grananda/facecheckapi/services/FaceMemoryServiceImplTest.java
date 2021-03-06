package com.grananda.facecheckapi.services;

import com.grananda.facecheckapi.domain.FaceMemory;
import com.grananda.facecheckapi.domain.FaceMemoryCollection;
import com.grananda.facecheckapi.domain.User;
import com.grananda.facecheckapi.exceptions.*;
import com.grananda.facecheckapi.repositories.FaceMemoryRepository;
import com.grananda.facecheckapi.utils.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import software.amazon.awssdk.services.rekognition.model.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
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
    void a_face_is_stored_in_a_collection() throws IOException, FaceCheckException {
        // Given
        FaceMemoryCollection faceMemoryCollection = FaceMemoryCollectionFactory.create();
        User user = UserFactory.create();
        Image image = ImageFactory.create("assets/image1a.jpg");
        FaceRecord faceRecord = FaceRecordFactory.create();

        IndexFacesResponse indexFacesResponse = IndexFacesResponse.builder()
                .faceRecords(faceRecord)
                .build();

        Set<FaceDetail> faceDetailList = new HashSet<>();
        faceDetailList.add(FaceDetailFactory.create());

        DetectFacesResponse detectFacesResponse = DetectFacesResponse.builder()
                .faceDetails(faceDetailList)
                .build();

        when(awsRekognitionFaceService.detectFaces(image)).thenReturn(detectFacesResponse);

        SearchFacesByImageResponse searchFacesByImageResponse = SearchFacesByImageResponse.builder().build();

        when(awsRekognitionFaceService.searchImage(faceMemoryCollection.getCollectionId(), image))
                .thenReturn(searchFacesByImageResponse);

        when(awsRekognitionFaceService.indexFace(faceMemoryCollection.getCollectionId(), image))
                .thenReturn(indexFacesResponse);

        // When
        FaceMemory faceMemory = faceMemoryService.storeFacialMemory(user, faceMemoryCollection, image);

        // Then
        assertEquals(faceMemory.getCollection().getCollectionId(), faceMemoryCollection.getCollectionId());
        assertEquals(faceMemory.getFaceId(), faceRecord.face().faceId());
        assertEquals(faceMemory.getUser().getEmail(), user.getEmail());

        verify(awsRekognitionFaceService).detectFaces(image);
        verify(awsRekognitionFaceService).searchImage(faceMemoryCollection.getCollectionId(), image);
        verify(awsRekognitionFaceService).indexFace(faceMemoryCollection.getCollectionId(), image);
    }

    @Test()
    void a_non_face_image_is_not_stored() throws IOException {
        // Given
        FaceMemoryCollection faceMemoryCollection = FaceMemoryCollectionFactory.create();
        User user = UserFactory.create();
        Image image = ImageFactory.create("assets/image4a.jpg");

        DetectFacesResponse detectFacesResponse = DetectFacesResponse.builder()
                .faceDetails(new HashSet<>())
                .build();

        when(awsRekognitionFaceService.detectFaces(image)).thenReturn(detectFacesResponse);

        // When
        Exception exception = assertThrows(MissingFaceInImageException.class, () -> {
            faceMemoryService.storeFacialMemory(user, faceMemoryCollection, image);
        });

        // Then
        assertTrue(exception.getMessage().contains("FaceMemoryException:FaceMemoryService:storeFacialMemory:"));
    }

    @Test
    void an_image_with_multiple_faces_is_not_stored() throws IOException, FaceCheckException {
        // Given
        FaceMemoryCollection faceMemoryCollection = FaceMemoryCollectionFactory.create();
        User user = UserFactory.create();
        Image image = ImageFactory.create("assets/image3a.jpg");

        FaceRecord faceRecord = FaceRecordFactory.create();

        IndexFacesResponse indexFacesResponse = IndexFacesResponse.builder()
                .faceRecords(faceRecord)
                .build();

        FaceDetail faceDetail1 = FaceDetailFactory.create();
        FaceDetail faceDetail2 = FaceDetailFactory.create();

        Set<FaceDetail> faceDetailList = new HashSet<>();
        faceDetailList.add(faceDetail1);
        faceDetailList.add(faceDetail2);

        DetectFacesResponse detectFacesResponse = DetectFacesResponse.builder()
                .faceDetails(faceDetailList)
                .build();

        when(awsRekognitionFaceService.detectFaces(image)).thenReturn(detectFacesResponse);

        when(awsRekognitionFaceService.indexFace(faceMemoryCollection.getCollectionId(), image))
                .thenReturn(indexFacesResponse);

        // When
        Exception exception = assertThrows(MultipleFacesInImageException.class, () -> {
            faceMemoryService.storeFacialMemory(user, faceMemoryCollection, image);
        });

        // Then
        assertTrue(exception.getMessage().contains("FaceMemoryException:FaceMemoryService:storeFacialMemory:"));
    }

    @Test
    void an_face_can_not_be_stored_multiple_times_once_indexed() throws IOException {
        // Given
        FaceMemoryCollection faceMemoryCollection = FaceMemoryCollectionFactory.create();
        User user = UserFactory.create();
        Image image = ImageFactory.create("assets/image1a.jpg");

        FaceMatch faceMatch = FaceMatchFactory.create();

        SearchFacesByImageResponse searchFacesByImageResponse = SearchFacesByImageResponse.builder()
                .faceMatches(faceMatch)
                .build();

        when(awsRekognitionFaceService.searchImage(faceMemoryCollection.getCollectionId(), image))
                .thenReturn(searchFacesByImageResponse);

        FaceDetail faceDetail = FaceDetailFactory.create();

        Set<FaceDetail> faceDetailList = new HashSet<>();
        faceDetailList.add(faceDetail);

        DetectFacesResponse detectFacesResponse = DetectFacesResponse.builder()
                .faceDetails(faceDetailList)
                .build();

        when(awsRekognitionFaceService.detectFaces(image)).thenReturn(detectFacesResponse);

        // When
        Exception exception = assertThrows(FaceAlreadyInCollectionException.class, () -> {
            faceMemoryService.storeFacialMemory(user, faceMemoryCollection, image);
        });

        // Then
        assertTrue(exception.getMessage().contains("FaceMemoryException:FaceMemoryService:storeFacialMemory:"));

    }

    @Test
    void a_face_is_removed_from_a_collection() throws FaceNotInCollectionException {
        // Given
        FaceMemoryCollection faceMemoryCollection = FaceMemoryCollectionFactory.create();
        FaceMemory faceMemory = FaceMemoryFactory.create();

        faceMemory.setCollection(faceMemoryCollection);
        faceMemoryCollection.getFaces().add(faceMemory);

        DeleteFacesResponse deleteFacesResponse = DeleteFacesResponse.builder()
                .deletedFaces(faceMemory.getFaceId())
                .build();

        Face face = Face.builder()
                .faceId(faceMemory.getFaceId())
                .build();

        ListFacesResponse listFacesResponse = ListFacesResponse.builder()
                .faces(face)
                .build();

        when(awsRekognitionFaceService.listCollectionFaces(faceMemoryCollection.getCollectionId()))
                .thenReturn(listFacesResponse);

        when(awsRekognitionFaceService.forgetFace(faceMemory.getFaceId(), faceMemoryCollection.getCollectionId()))
                .thenReturn(deleteFacesResponse);

        // When
        boolean response = faceMemoryService.removeFacialMemory(faceMemory);

        // Then
        assertTrue(response);
    }

    @Test
    void a_non_existing_face_can_not_be_removed_from_a_collection() {
        // Given
        FaceMemoryCollection faceMemoryCollection = FaceMemoryCollectionFactory.create();
        FaceMemory faceMemory = FaceMemoryFactory.create();

        faceMemory.setCollection(faceMemoryCollection);
        faceMemoryCollection.getFaces().add(faceMemory);

        when(awsRekognitionFaceService.searchFace(faceMemoryCollection.getCollectionId(), faceMemory.getFaceId()))
                .thenThrow(InvalidParameterException.class);

        // When
        Exception exception = assertThrows(FaceNotInCollectionException.class, () -> {
            faceMemoryService.removeFacialMemory(faceMemory);
        });

        // Then
        assertTrue(exception.getMessage().contains("FaceMemoryException:FaceMemoryService:removeFacialMemory:"));
    }
}