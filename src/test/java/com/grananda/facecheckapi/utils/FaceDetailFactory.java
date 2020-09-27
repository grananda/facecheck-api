package com.grananda.facecheckapi.utils;

import com.github.javafaker.Faker;
import software.amazon.awssdk.services.rekognition.model.FaceDetail;

public class FaceDetailFactory {
    public static FaceDetail create() {
        return FaceDetail.builder()
                .confidence(((float) Faker.instance().number().numberBetween(70, 99)))
                .build();
    }
}
