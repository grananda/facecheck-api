package com.grananda.facecheckapi.services;

import com.grananda.facecheckapi.domain.FaceMemoryCollection;
import com.grananda.facecheckapi.domain.Organization;

public interface FacialMemoryCollectionService {

    FaceMemoryCollection registerFaceMemoryCollection(Organization organization, String collectionName);

    Boolean removeFaceMemoryCollection(FaceMemoryCollection collection);
}
