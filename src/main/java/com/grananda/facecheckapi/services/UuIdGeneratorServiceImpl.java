package com.grananda.facecheckapi.services;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UuIdGeneratorServiceImpl implements UuIdGeneratorService {
    @Override
    public UUID generateUuId() {
        return UUID.randomUUID();
    }
}
