package com.yawarSoft.interoperability.Entities;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "units")
public class UnitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stamp_pronahebas", length = 6)
    private String stampPronahebas;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_blood_bank_actual")
    private BloodBankEntity bloodBank;

    @Column(name = "unit_type", nullable = false, length = 50)
    private String unitType;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal volume;

    @Column(name = "blood_type", nullable = false, length = 10)
    private String bloodType;

    @Column(name = "anticoagulant")
    private String anticoagulant;

    @Column(name = "bag_type")
    private String bagType;

    @Column(name = "label_url")
    private String labelUrl;

    @Column(name = "entry_date")
    private LocalDate entryDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "serology_result")
    private String serologyResult;

    @Column(name = "reason_discard")
    private String reasonDiscard;

    @Column(name = "from_donation")
    private Boolean fromDonation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by", referencedColumnName = "id", nullable = false)
    private UserEntity createdBy;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    private UserEntity updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "discard_by", referencedColumnName = "id")
    private UserEntity discardBy;

    @Column(name = "discard_at")
    private LocalDateTime discardAt;
}
