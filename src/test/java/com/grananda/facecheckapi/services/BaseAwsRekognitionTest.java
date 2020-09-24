package com.grananda.facecheckapi.services;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.rekognition.model.Image;
import software.amazon.awssdk.services.rekognition.model.ListCollectionsResponse;

import java.io.*;
import java.util.UUID;

@SpringBootTest
public class BaseAwsRekognitionTest {

    @Autowired
    AwsRekognitionCollectionService rekognitionCollectionService;

    @AfterEach
    private void cleanUp() {
        ListCollectionsResponse response = rekognitionCollectionService.listFaceMemoryCollections();

        for (String collectionId : response.collectionIds()) {
            System.out.println("DELETING COLLECTION: " + collectionId);
            rekognitionCollectionService.deleteFaceMemoryCollection(collectionId);
        }
    }

    /**
     * @return String
     */
    protected String createCollection() {
        String collectionId = UUID.randomUUID().toString();

        rekognitionCollectionService.createFaceMemoryCollection(collectionId);

        return collectionId;
    }

    /**
     * @param sourceImagePath
     * @return Image
     * @throws FileNotFoundException
     */
    protected Image processSourceImage(String sourceImagePath) throws IOException {
        String path = "src/test/resources/";

        File file = new File(path + sourceImagePath);
        InputStream sourceStream = new FileInputStream(file);
        SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceStream);

        return Image.builder()
                .bytes(sourceBytes)
                .build();
    }
}
