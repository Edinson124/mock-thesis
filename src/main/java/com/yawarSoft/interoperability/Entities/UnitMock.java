package com.yawarSoft.interoperability.Entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitMock {
    private String id;
    private String tipo;           // Ej: "Concentrado de hematíes", "Plasma fresco"
    private BloodTypeMock grupoSanguineo; // Ej: "A+", "O-"
    private String fechaVencimiento;
    private String estado;
}
