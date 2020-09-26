package com.grananda.facecheckapi.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.util.Collection;

@Log4j2
@Service
public class AwsRekognitionFaceServiceImpl implements AwsRekognitionFaceService {

    private final RekognitionClient client;

    public AwsRekognitionFaceServiceImpl(RekognitionClient client) {
        this.client = client;
    }

    @Override
    public DetectFacesResponse detectFaces(Image image) {
        DetectFacesRequest request = DetectFacesRequest.builder()
                .image(image)
                .build();

        return client.detectFaces(request);
    }

    @Override
    public IndexFacesResponse indexFace(String collectionId, Image image) {
        IndexFacesRequest request = IndexFacesRequest.builder()
                .collectionId(collectionId)
                .image(image)
                .maxFaces(1)
                .qualityFilter(QualityFilter.AUTO)
                .detectionAttributes(Attribute.ALL)
                .build();

        return client.indexFaces(request);
    }

    @Override
    public SearchFacesByImageResponse identifyFace(String collectionId, Image image) {
        SearchFacesByImageRequest request = SearchFacesByImageRequest.builder()
                .collectionId(collectionId)
                .image(image)
                .maxFaces(1)
                .faceMatchThreshold(90F)
                .build();

        return client.searchFacesByImage(request);
    }

    @Override
    public DeleteFacesResponse forgetFace(String collectionId, String faceId) {
        DeleteFacesRequest request = DeleteFacesRequest.builder()
                .collectionId(collectionId)
                .faceIds(faceId)
                .build();

        return client.deleteFaces(request);
    }

    public DeleteFacesResponse forgetFaces(String collectionId, Collection<String> faceIds) {
        DeleteFacesRequest request = DeleteFacesRequest.builder()
                .collectionId(collectionId)
                .faceIds(faceIds)
                .build();

        return client.deleteFaces(request);
    }

    @Override
    public ListFacesResponse listCollectionFaces(String collectionId) {
        ListFacesRequest request = ListFacesRequest.builder()
                .collectionId(collectionId)
                .build();

        return client.listFaces(request);
    }
}
