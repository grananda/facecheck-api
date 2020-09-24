package com.grananda.facecheckapi.repositories;

import com.grananda.facecheckapi.domain.FaceMemoryCollection;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface FaceMemoryCollectionRepository extends CrudRepository<FaceMemoryCollection, UUID> {
}
