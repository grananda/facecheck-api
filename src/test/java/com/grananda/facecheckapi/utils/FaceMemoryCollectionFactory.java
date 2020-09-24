package com.grananda.facecheckapi.utils;

import com.github.javafaker.Faker;
import com.grananda.facecheckapi.domain.FaceMemoryCollection;

import java.util.HashSet;
import java.util.UUID;

public class FaceMemoryCollectionFactory {
    public static FaceMemoryCollection create() {
        return FaceMemoryCollection.builder()
                .collectionId(UUID.randomUUID().toString())
                .name(Faker.instance().lorem().word())
                .collectionArn(Faker.instance().lorem().word())
                .faces(new HashSet<>())
                .build();
    }
}
