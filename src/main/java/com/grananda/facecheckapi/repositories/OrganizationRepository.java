package com.grananda.facecheckapi.repositories;

import com.grananda.facecheckapi.domain.Organization;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OrganizationRepository extends CrudRepository<Organization, UUID> {
}
