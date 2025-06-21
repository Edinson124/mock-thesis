package com.yawarSoft.interoperability.Repositories;

import com.yawarSoft.interoperability.Dtos.StockResumenDTO;
import com.yawarSoft.interoperability.Entities.UnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<UnitEntity, Long>, JpaSpecificationExecutor<UnitEntity> {


    @Query("""
        SELECT new com.yawarSoft.interoperability.Dtos.StockResumenDTO(
            u.bloodType,
            u.unitType,
            COUNT(u)
        )
        FROM UnitEntity u
        WHERE u.status = 'Apto'
          AND u.bloodBank.id = :bloodBankId
        GROUP BY u.bloodType, u.unitType
        ORDER BY u.bloodType, u.unitType
    """)
    List<StockResumenDTO> getResumenStockByBloodBank(@Param("bloodBankId") Integer bloodBankId);
}