package com.grananda.facecheckapi.repositories;

import com.grananda.facecheckapi.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
}
