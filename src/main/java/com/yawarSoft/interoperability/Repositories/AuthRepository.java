package com.yawarSoft.interoperability.Repositories;

import com.yawarSoft.interoperability.Entities.AuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<AuthEntity,Integer> {
    Optional<AuthEntity> findByUsername(String username);
}
