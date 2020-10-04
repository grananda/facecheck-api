package com.grananda.facecheckapi.utils;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.rekognition.model.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageFactory {

    public static Image create(String sourceImagePath) throws IOException {
        String path = "src/test/resources/";

        File file = new File(path + sourceImagePath);
        InputStream sourceStream = new FileInputStream(file);
        SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceStream);

        return Image.builder()
                .bytes(sourceBytes)
                .build();
    }
}
