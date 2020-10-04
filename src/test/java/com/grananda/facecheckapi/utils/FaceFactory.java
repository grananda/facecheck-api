package com.grananda.facecheckapi.utils;

import software.amazon.awssdk.services.rekognition.model.Face;

import java.util.UUID;

public class FaceFactory {
    public static Face create(String faceId) {
                return Face.builder()
                .faceId(faceId)
                .build();
    }

    public static Face create() {
        return Face.builder()
                .faceId(UUID.randomUUID().toString())
                .build();
    }
}
