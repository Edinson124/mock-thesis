package com.yawarSoft.interoperability.Dtos;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockResumenDTO{
    private String bloodType;
    private String unitType;
    private Long quantity;
}