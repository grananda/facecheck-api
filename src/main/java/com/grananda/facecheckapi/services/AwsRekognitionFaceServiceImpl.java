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
        DetectFacesResponse response = DetectFacesResponse.builder().build();

        try {
            DetectFacesRequest request = DetectFacesRequest.builder()
                    .image(image)
                    .build();

            response = client.detectFaces(request);
        } catch (Exception awsException) {
            log.error("AwsSdkError:AwsRekognitionFaceServiceImpl:DetectFacesResponse: " + awsException.toString());
        }

        return response;
    }

    @Override
    public IndexFacesResponse indexFace(String collectionId, Image image) {
        IndexFacesResponse response = IndexFacesResponse.builder().build();

        try {
            IndexFacesRequest request = IndexFacesRequest.builder()
                    .collectionId(collectionId)
                    .image(image)
                    .maxFaces(1)
                    .qualityFilter(QualityFilter.AUTO)
                    .detectionAttributes(Attribute.ALL)
                    .build();

            response = client.indexFaces(request);
        } catch (Exception awsException) {
            log.error("AwsSdkError:AwsRekognitionFaceServiceImpl:IndexFacesResponse: " + awsException.toString());
        }

        return response;
    }

    @Override
    public SearchFacesByImageResponse identifyFace(String collectionId, Image image) {
        SearchFacesByImageResponse response = SearchFacesByImageResponse.builder().build();

        try {
            SearchFacesByImageRequest request = SearchFacesByImageRequest.builder()
                    .collectionId(collectionId)
                    .image(image)
                    .maxFaces(1)
                    .faceMatchThreshold(90F)
                    .build();

            response = client.searchFacesByImage(request);
        } catch (Exception awsException) {
            log.error("AwsSdkError:AwsRekognitionFaceServiceImpl:SearchFacesByImageResponse: " + awsException.toString());
        }

        return response;
    }

    @Override
    public DeleteFacesResponse forgetFace(String collectionId, String faceId) {
        DeleteFacesResponse response = DeleteFacesResponse.builder().build();

        try {
            DeleteFacesRequest request = DeleteFacesRequest.builder()
                    .collectionId(collectionId)
                    .faceIds(faceId)
                    .build();

            response = client.deleteFaces(request);
        } catch (Exception awsException) {
            log.error("AwsSdkError:AwsRekognitionFaceServiceImpl:DeleteFacesResponse: " + awsException.toString());
        }

        return response;
    }

    public DeleteFacesResponse forgetFaces(String collectionId, Collection<String> faceIds) {
        DeleteFacesResponse response = DeleteFacesResponse.builder().build();

        try {
            DeleteFacesRequest request = DeleteFacesRequest.builder()
                    .collectionId(collectionId)
                    .faceIds(faceIds)
                    .build();

            response = client.deleteFaces(request);
        } catch (Exception awsException) {
            log.error("AwsSdkError:AwsRekognitionFaceServiceImpl:DeleteFacesResponse: " + awsException.toString());
        }

        return response;
    }

    @Override
    public ListFacesResponse listCollectionFaces(String collectionId) {
        ListFacesResponse response = ListFacesResponse.builder().build();

        try {
            ListFacesRequest request = ListFacesRequest.builder()
                    .collectionId(collectionId)
                    .build();

            response = client.listFaces(request);
        } catch (Exception awsException) {
            log.error("AwsSdkError:AwsRekognitionFaceServiceImpl:DeleteFacesResponse: " + awsException.toString());
        }

        return response;
    }
}
