package com.yawarSoft.interoperability.Repositories;

import com.yawarSoft.interoperability.Entities.BloodBankNetworkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodBankNetworkRepository extends JpaRepository<BloodBankNetworkEntity,Integer>, JpaSpecificationExecutor<BloodBankNetworkEntity> {

    @Query("""
       SELECT COUNT(bbn)
       FROM BloodBankNetworkEntity bbn
       WHERE bbn.status = :status
       AND bbn.network.id IN (
           SELECT bbn2.network.id
           FROM BloodBankNetworkEntity bbn2
           WHERE bbn2.bloodBank.id = :loggedInBloodBankId
           AND bbn2.status = :status
       )
       AND bbn.bloodBank.id = :targetBloodBankId
      """)
    long countActiveRelationsBetweenBanks(
            @Param("loggedInBloodBankId") Integer loggedInBloodBankId,
            @Param("targetBloodBankId") Integer targetBloodBankId,
            @Param("status") String status
    );

}
