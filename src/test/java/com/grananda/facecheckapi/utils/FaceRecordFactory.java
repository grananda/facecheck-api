package com.grananda.facecheckapi.utils;

import software.amazon.awssdk.services.rekognition.model.FaceRecord;

public class FaceRecordFactory {
    public static FaceRecord create() {
        return FaceRecord.builder()
                .face(FaceFactory.create())
                .build();
    }
}
