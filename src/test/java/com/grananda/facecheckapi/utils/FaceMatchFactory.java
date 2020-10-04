package com.grananda.facecheckapi.utils;

import software.amazon.awssdk.services.rekognition.model.FaceMatch;

public class FaceMatchFactory {
    public static FaceMatch create() {
        return FaceMatch.builder()
                .face(FaceFactory.create())
                .build();
    }
}
