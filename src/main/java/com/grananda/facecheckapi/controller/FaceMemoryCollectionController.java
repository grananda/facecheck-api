package com.grananda.facecheckapi.controller;

import com.grananda.facecheckapi.domain.FaceMemoryCollection;
import com.grananda.facecheckapi.services.FaceMemoryCollectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/organizations/{organizationId}/collections")
@RestController
public class FaceMemoryCollectionController {

    private final FaceMemoryCollectionService faceMemoryCollectionService;

    public FaceMemoryCollectionController(FaceMemoryCollectionService faceMemoryCollectionService) {
        this.faceMemoryCollectionService = faceMemoryCollectionService;
    }

    @GetMapping()
    public ResponseEntity<List<FaceMemoryCollection>> index() {

    }
}
