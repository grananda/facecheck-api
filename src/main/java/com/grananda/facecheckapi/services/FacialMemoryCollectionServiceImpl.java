package com.grananda.facecheckapi.services;

import com.grananda.facecheckapi.domain.FaceMemoryCollection;
import com.grananda.facecheckapi.domain.Organization;
import com.grananda.facecheckapi.repositories.FaceMemoryCollectionRepository;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rekognition.model.CreateCollectionResponse;
import software.amazon.awssdk.services.rekognition.model.DeleteCollectionResponse;

@Service
public class FacialMemoryCollectionServiceImpl implements FacialMemoryCollectionService {

    private final AwsRekognitionCollectionService awsRekognitionCollectionService;

    private final FaceMemoryCollectionRepository faceMemoryCollectionRepository;

    private final UuIdGeneratorService uuIdGeneratorService;

    public FacialMemoryCollectionServiceImpl(
            AwsRekognitionCollectionService awsRekognitionCollectionService,
            FaceMemoryCollectionRepository faceMemoryCollectionRepository,
            UuIdGeneratorService uuIdGeneratorService
    ) {
        this.awsRekognitionCollectionService = awsRekognitionCollectionService;
        this.faceMemoryCollectionRepository = faceMemoryCollectionRepository;
        this.uuIdGeneratorService = uuIdGeneratorService;
    }

    @Override
    public FaceMemoryCollection registerFaceMemoryCollection(Organization organization, String collectionName) {
        String collectionId = uuIdGeneratorService.generateUuId().toString();

        CreateCollectionResponse createCollectionResponse = awsRekognitionCollectionService.createFaceMemoryCollection(collectionId);

        FaceMemoryCollection faceMemoryCollection = FaceMemoryCollection.builder()
                .name(collectionName)
                .collectionId(collectionId)
                .collectionArn(createCollectionResponse.collectionArn())
                .build();

        faceMemoryCollectionRepository.save(faceMemoryCollection);

        faceMemoryCollection.setOrganization(organization);
        organization.getMemoryCollections().add(faceMemoryCollection);

        return faceMemoryCollection;
    }

    @Override
    public Boolean removeFaceMemoryCollection(FaceMemoryCollection collection) {
        DeleteCollectionResponse deleteCollectionResponse = awsRekognitionCollectionService.deleteFaceMemoryCollection(collection.getCollectionId());

        if (deleteCollectionResponse.statusCode() == awsRekognitionCollectionService.AWS_SUCCESS_STATUS_CODE) {
            faceMemoryCollectionRepository.delete(collection);

            return true;
        }

        return false;
    }
}
