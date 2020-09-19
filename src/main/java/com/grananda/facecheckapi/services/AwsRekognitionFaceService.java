package com.grananda.facecheckapi.services;

import software.amazon.awssdk.services.rekognition.model.*;

import java.util.Collection;

public interface AwsRekognitionFaceService {

    DetectFacesResponse detectFaces(Image image);

    IndexFacesResponse indexFace(String collectionId, Image image);

    SearchFacesByImageResponse identifyFace(String collectionId, Image image);

    DeleteFacesResponse forgetFace(String collectionId, String faceId);

    DeleteFacesResponse forgetFaces(String collectionId, Collection<String> faceIds);

    ListFacesResponse listCollectionFaces(String collectionId);
}
