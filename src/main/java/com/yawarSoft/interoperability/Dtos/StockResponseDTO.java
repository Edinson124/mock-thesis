package com.yawarSoft.interoperability.Dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockResponseDTO {
    private String grupoSanguineo;
    private String rh;
    private String tipoUnidad;
    private Long cantidad;

    @Override
    public String toString() {
        return "StockResponseDTO[grupoSanguineo=" + grupoSanguineo + ", rh=" + rh + ", tipoUnidad=" + tipoUnidad + ", cantidad=" + cantidad + "]";
    }
}

