package com.grananda.facecheckapi.utils;

import com.grananda.facecheckapi.domain.FaceMemory;

import java.util.UUID;

public class FaceMemoryFactory {

    public static FaceMemory create() {
        return FaceMemory.builder()
                .faceId(UUID.randomUUID().toString())
                .build();
    }
}
