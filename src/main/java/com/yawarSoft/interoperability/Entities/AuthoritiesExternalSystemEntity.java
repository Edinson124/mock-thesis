package com.yawarSoft.interoperability.Entities;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "authorities_external_system")
public class AuthoritiesExternalSystemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "authority", nullable = false)
    private String authority;

    @Column(name = "http_method", nullable = false, length = 10)
    private String httpMethod;

    @Column(name = "api_path", nullable = false)
    private String apiPath;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
