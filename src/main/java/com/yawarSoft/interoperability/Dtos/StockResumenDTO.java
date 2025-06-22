package com.yawarSoft.interoperability.Dtos;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockResumenDTO{
    private String sangre;
    private String rh;
    private String tipo;
    private Long cantidad;
}