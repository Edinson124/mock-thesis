package com.yawarSoft.interoperability.Entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitMock {
    private String id;
    private String tipo;
    private BloodTypeMock grupoSanguineo;
    private String fechaVencimiento;
    private String estado;
}
