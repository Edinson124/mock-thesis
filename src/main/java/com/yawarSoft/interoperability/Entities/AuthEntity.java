package com.yawarSoft.interoperability.Entities;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "auth")
public class AuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username;
    private String password;

    @OneToOne
    @JoinColumn(name = "id_user", nullable = false)
    private UserEntity user;

    //Atributos de configuracion para SpringSecurity
    @Column(name = "is_enabled")
    private boolean isEnabled = true;
    @Column(name = "account_No_Expired")
    private boolean accountNoExpired = true;
    @Column(name = "account_No_Locked")
    private boolean accountNoLocked = true;
    @Column(name = "credential_No_Expired")
    private boolean credentialNoExpired = true;
}
