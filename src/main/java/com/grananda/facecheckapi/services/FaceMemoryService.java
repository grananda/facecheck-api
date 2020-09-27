package com.grananda.facecheckapi.services;

import com.grananda.facecheckapi.domain.FaceMemory;
import com.grananda.facecheckapi.domain.FaceMemoryCollection;
import com.grananda.facecheckapi.domain.User;
import com.grananda.facecheckapi.exceptions.FaceCheckException;
import com.grananda.facecheckapi.exceptions.FaceNotInCollectionException;
import software.amazon.awssdk.services.rekognition.model.Image;

public interface FaceMemoryService {

    FaceMemory storeFacialMemory(User user, FaceMemoryCollection collection, Image image) throws FaceCheckException;

    boolean removeFacialMemory(FaceMemory faceMemory) throws FaceNotInCollectionException;
}
