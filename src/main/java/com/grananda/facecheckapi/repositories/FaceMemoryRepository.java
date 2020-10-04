package com.grananda.facecheckapi.repositories;

import com.grananda.facecheckapi.domain.FaceMemory;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface FaceMemoryRepository extends CrudRepository<FaceMemory, UUID> {
}
