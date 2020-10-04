package com.grananda.facecheckapi.services;

import com.grananda.facecheckapi.domain.FaceMemory;
import com.grananda.facecheckapi.domain.FaceMemoryCollection;
import com.grananda.facecheckapi.domain.User;
import com.grananda.facecheckapi.exceptions.*;
import com.grananda.facecheckapi.repositories.FaceMemoryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rekognition.model.*;

@Log4j2
@Service
public class FaceMemoryServiceImpl implements FaceMemoryService {

    private final AwsRekognitionFaceService awsRekognitionFaceService;

    private final FaceMemoryRepository faceMemoryRepository;

    public FaceMemoryServiceImpl(AwsRekognitionFaceService awsRekognitionFaceService, FaceMemoryRepository faceMemoryRepository) {
        this.awsRekognitionFaceService = awsRekognitionFaceService;
        this.faceMemoryRepository = faceMemoryRepository;
    }

    @Override
    public FaceMemory storeFacialMemory(User user, FaceMemoryCollection collection, Image image) throws FaceCheckException {

        DetectFacesResponse detectFacesResponse = awsRekognitionFaceService.detectFaces(image);

        if (detectFacesResponse.faceDetails().size() == 0) {
            throw new MissingFaceInImageException("FaceMemoryException:FaceMemoryService:storeFacialMemory: No faces were detected in provided image for user " + user.getId());
        }

        if (detectFacesResponse.faceDetails().size() > 1) {
            throw new MultipleFacesInImageException("FaceMemoryException:FaceMemoryService:storeFacialMemory: Multiple faces were detected in provided image for user " + user.getId());
        }

        SearchFacesByImageResponse searchFacesByImageResponse = awsRekognitionFaceService.searchImage(collection.getCollectionId(), image);

        if (searchFacesByImageResponse.faceMatches().size() > 0) {
            throw new FaceAlreadyInCollectionException("FaceMemoryException:FaceMemoryService:storeFacialMemory: Face already exists into collection " + user.getId());
        }

        IndexFacesResponse response = awsRekognitionFaceService.indexFace(collection.getCollectionId(), image);

        FaceRecord faceRecord = response.faceRecords().iterator().next();

        FaceMemory faceMemory = FaceMemory.builder()
                .faceId(faceRecord.face().faceId())
                .build();

        faceMemoryRepository.save(faceMemory);

        faceMemory.setCollection(collection);
        faceMemory.setUser(user);

        collection.getFaces().add(faceMemory);

        user.setFaceMemory(faceMemory);

        return faceMemory;
    }

    @Override
    public boolean removeFacialMemory(FaceMemory faceMemory) throws FaceNotInCollectionException {
        try {
            awsRekognitionFaceService.searchFace(faceMemory.getCollection().getCollectionId(), faceMemory.getFaceId());
        } catch (InvalidParameterException invalidParameterException) {
            throw new FaceNotInCollectionException("FaceMemoryException:FaceMemoryService:removeFacialMemory: Face is not in collection " + faceMemory.getFaceId(), invalidParameterException);
        }

        DeleteFacesResponse deleteFacesResponse = awsRekognitionFaceService.forgetFace(faceMemory.getFaceId(), faceMemory.getCollection().getCollectionId());

        long deletedFaces = deleteFacesResponse.deletedFaces()
                .stream()
                .filter(item -> item.equals(faceMemory.getFaceId()))
                .count();

        return deletedFaces > 0L;
    }
}
