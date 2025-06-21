package com.yawarSoft.interoperability.Repositories;

import com.yawarSoft.interoperability.Entities.AuthExternalSystemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthExternalSystemRepository extends JpaRepository<AuthExternalSystemEntity, Integer> {
    Optional<AuthExternalSystemEntity> findByClientUser(String clientUser);
}

