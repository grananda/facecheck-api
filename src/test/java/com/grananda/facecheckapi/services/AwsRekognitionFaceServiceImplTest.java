package com.grananda.facecheckapi.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.rekognition.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AwsRekognitionFaceServiceImplTest extends BaseAwsRekognitionTest {

    @Autowired
    AwsRekognitionFaceService rekognitionFaceService;

    @Test
    void a_face_is_detected() throws IOException {
        // Given
        Image image = processSourceImage("assets/image1a.jpg");

        // When
        DetectFacesResponse response = rekognitionFaceService.detectFaces(image);

        // Then
        assertEquals(1, response.faceDetails().size());
    }

    @Test
    void a_face_is_not_detected() throws IOException {
        // Given
        Image image = processSourceImage("assets/image4a.jpg");

        // When
        DetectFacesResponse response = rekognitionFaceService.detectFaces(image);

        // Then
        assertEquals(0, response.faceDetails().size());
    }

    @Test
    void multiple_faces_are_detected() throws IOException {
        // Given
        Image image = processSourceImage("assets/image3a.jpg");

        // When
        DetectFacesResponse response = rekognitionFaceService.detectFaces(image);

        // Then
        assertEquals(9, response.faceDetails().size());
    }

    @Test
    void a_face_is_indexed() throws IOException {
        // Given
        Image image = processSourceImage("assets/image1a.jpg");

        String collectionId = createCollection();

        // When
        IndexFacesResponse response = rekognitionFaceService.indexFace(collectionId, image);

        // Then
        assertEquals(1, response.faceRecords().size());
    }

    @Test
    void an_image_without_face_is_not_indexed() throws IOException {
        // Given
        Image image = processSourceImage("assets/image4a.jpg");

        String collectionId = createCollection();

        // When
        IndexFacesResponse response = rekognitionFaceService.indexFace(collectionId, image);

        // Then
        assertEquals(0, response.faceRecords().size());
    }

    @Test
    void a_face_is_identified() throws IOException {
        // Given
        Image image = processSourceImage("assets/image1a.jpg");

        Image imageToIdentify = processSourceImage("assets/image1b.jpg");

        String collectionId = createCollection();

        rekognitionFaceService.indexFace(collectionId, image);

        // When
        SearchFacesByImageResponse response = rekognitionFaceService.identifyFace(collectionId, imageToIdentify);

        Face face = response.faceMatches().iterator().next().face();

        // Then
        assertTrue(face.confidence() > 90);
    }

    @Test
    void a_non_indexed_face_is_not_identified() throws IOException {
        // Given
        Image image = processSourceImage("assets/image1a.jpg");

        Image unIndexedImage = processSourceImage("assets/image2a.jpg");

        String collectionId = createCollection();

        rekognitionFaceService.indexFace(collectionId, image);

        // When
        SearchFacesByImageResponse response = rekognitionFaceService.identifyFace(collectionId, unIndexedImage);

        // Then
        assertEquals(0, response.faceMatches().size());
    }

    @Test
    void a_face_is_forgotten() throws IOException {
        // Given
        Image image = processSourceImage("assets/image1a.jpg");

        String collectionId = createCollection();

        IndexFacesResponse indexFacesResponse = rekognitionFaceService.indexFace(collectionId, image);

        Face face = indexFacesResponse.faceRecords().iterator().next().face();

        String imageId = face.faceId();

        // When
        DeleteFacesResponse response = rekognitionFaceService.forgetFace(collectionId, imageId);

        // Then
        assertTrue(response.hasDeletedFaces());
    }

    @Test
    void a_list_of_faces_are_forgotten() throws IOException {
        // Given
        Image image1 = processSourceImage("assets/image1a.jpg");

        Image image2 = processSourceImage("assets/image2a.jpg");

        String collectionId = createCollection();

        IndexFacesResponse indexFacesResponse1 = rekognitionFaceService.indexFace(collectionId, image1);

        Face face1 = indexFacesResponse1.faceRecords().iterator().next().face();

        String imageId1 = face1.faceId();

        IndexFacesResponse indexFacesResponse2 = rekognitionFaceService.indexFace(collectionId, image2);

        Face face2 = indexFacesResponse2.faceRecords().iterator().next().face();

        String imageId2 = face2.faceId();

        Collection<String> faces = new ArrayList<>();
        faces.add(imageId1);
        faces.add(imageId2);

        // When
        DeleteFacesResponse response = rekognitionFaceService.forgetFaces(collectionId, faces);

        // Then
        assertTrue(response.hasDeletedFaces());
    }

    @Test
    void a_list_of_faces_per_collection_is_requested() throws IOException {
        // Given
        Image image = processSourceImage("assets/image1a.jpg");

        String collectionId = createCollection();

        rekognitionFaceService.indexFace(collectionId, image);

        // When
        ListFacesResponse response = rekognitionFaceService.listCollectionFaces(collectionId);

        // Then
        assertTrue(response.hasFaces());
    }
}