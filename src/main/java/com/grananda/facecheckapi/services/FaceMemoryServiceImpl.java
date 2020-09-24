package com.grananda.facecheckapi.services;

import com.grananda.facecheckapi.domain.FaceMemory;
import com.grananda.facecheckapi.domain.FaceMemoryCollection;
import com.grananda.facecheckapi.domain.User;
import com.grananda.facecheckapi.repositories.FaceMemoryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rekognition.model.DeleteFacesResponse;
import software.amazon.awssdk.services.rekognition.model.FaceRecord;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.IndexFacesResponse;

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
    public FaceMemory storeFacialMemory(User user, FaceMemoryCollection collection, Image image) {
        FaceMemory faceMemory = FaceMemory.builder().build();

        try {
            IndexFacesResponse response = awsRekognitionFaceService.indexFace(collection.getCollectionId(), image);

            FaceRecord faceRecord = response.faceRecords().iterator().next();

            faceMemory = FaceMemory.builder()
                    .faceId(faceRecord.face().faceId())
                    .build();

            faceMemoryRepository.save(faceMemory);

            faceMemory.setCollection(collection);
            faceMemory.setUser(user);

            collection.getFaces().add(faceMemory);

            user.setFaceMemory(faceMemory);
        } catch (Exception faceMemoryException) {
            log.error("FaceMemoryException:FaceMemoryService:storeFacialMemory: " + faceMemoryException.toString());
        }

        return faceMemory;
    }

    @Override
    public boolean removeFacialMemory(FaceMemory faceMemory) {
        try {
            DeleteFacesResponse deleteFacesResponse = awsRekognitionFaceService.forgetFace(faceMemory.getFaceId(), faceMemory.getCollection().getCollectionId());

            long deletedFaces = deleteFacesResponse.deletedFaces().stream()
                    .filter(item -> item.equals(faceMemory.getFaceId()))
                    .count();

            if (deletedFaces > 0L) {
                return true;
            }

        } catch (Exception faceMemoryException) {
            log.error("FaceMemoryException:FaceMemoryService:removeFacialMemory: " + faceMemoryException.toString());
        }

        return false;
    }
}
