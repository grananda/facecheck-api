package com.grananda.facecheckapi.services;

import com.grananda.facecheckapi.domain.FaceMemory;
import com.grananda.facecheckapi.domain.FaceMemoryCollection;
import com.grananda.facecheckapi.domain.User;
import software.amazon.awssdk.services.rekognition.model.Image;

public interface FaceMemoryService {

    FaceMemory storeFacialMemory(User user, FaceMemoryCollection collection, Image image);

    boolean removeFacialMemory(FaceMemory faceMemory);
}
