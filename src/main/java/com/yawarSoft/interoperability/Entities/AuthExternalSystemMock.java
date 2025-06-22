package com.yawarSoft.interoperability.Entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthExternalSystemMock {

    private String uuid;
    private String usuario;
    private String contrasena;
    private String estado;
}
