package com.grananda.facecheckapi.services;

import com.grananda.facecheckapi.domain.FaceMemoryCollection;
import com.grananda.facecheckapi.domain.Organization;
import com.grananda.facecheckapi.exceptions.UnknownCollectionException;
import com.grananda.facecheckapi.repositories.FaceMemoryCollectionRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rekognition.model.CreateCollectionResponse;
import software.amazon.awssdk.services.rekognition.model.DeleteCollectionResponse;
import software.amazon.awssdk.services.rekognition.model.ResourceNotFoundException;

@Log4j2
@Service
public class FaceMemoryCollectionServiceImpl implements FaceMemoryCollectionService {

    private final AwsRekognitionCollectionService awsRekognitionCollectionService;

    private final FaceMemoryCollectionRepository faceMemoryCollectionRepository;

    private final UuIdGeneratorService uuIdGeneratorService;

    public FaceMemoryCollectionServiceImpl(
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
    public Boolean removeFaceMemoryCollection(FaceMemoryCollection collection) throws UnknownCollectionException {
        try {

            DeleteCollectionResponse deleteCollectionResponse = awsRekognitionCollectionService.deleteFaceMemoryCollection(collection.getCollectionId());

            if (deleteCollectionResponse.statusCode() == awsRekognitionCollectionService.AWS_SUCCESS_STATUS_CODE) {

                return true;
            }
        } catch (ResourceNotFoundException resourceNotFoundException) {
            throw new UnknownCollectionException("FaceMemoryException:FaceMemoryCollectionService:removeFaceMemoryCollection: Can not find requested collection: " + collection.getCollectionId());
        } finally {
            faceMemoryCollectionRepository.delete(collection);
        }

        return false;
    }
}
