package com.yawarSoft.interoperability.Repositories;

import com.yawarSoft.interoperability.Entities.BloodBankEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodBankRepository extends JpaRepository<BloodBankEntity,Integer>, JpaSpecificationExecutor<BloodBankEntity> {
    Page<BloodBankEntity> findByStatusAndIsInternal(String status, Boolean isInternal, Pageable pageable);

    @Query("""
            SELECT DISTINCT bb
            FROM BloodBankEntity bb
            JOIN BloodBankNetworkEntity bbn ON bb.id = bbn.bloodBank.id
            WHERE bbn.network.id IN (
                SELECT bbn2.network.id
                FROM BloodBankNetworkEntity bbn2
                WHERE bbn2.bloodBank.id = :bloodBankId
                      AND bbn2.status = :status
            )
            AND bb.isInternal = true
            AND bbn.status = :status
            """)
    Page<BloodBankEntity> findAllBySameNetworkAsBloodBankAndInternalTrueAndStatus(
            @Param("bloodBankId") Integer bloodBankId,
            @Param("status") String status,
            Pageable pageable
    );
}