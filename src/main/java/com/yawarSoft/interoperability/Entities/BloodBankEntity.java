package com.yawarSoft.interoperability.Entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "blood_banks")
public class BloodBankEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String region;
    private String province;
    private String district;
    private String address;
    private String status;
    @Column(name = "is_internal")
    private Boolean isInternal;
    @Column(name = "profile_image_url")
    private String profileImageUrl;
    @ManyToOne
    @JoinColumn(name = "id_blood_bank_type", nullable = false)
    private BloodBankTypeEntity bloodBankType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_coordinator")
    private UserEntity coordinator;
}