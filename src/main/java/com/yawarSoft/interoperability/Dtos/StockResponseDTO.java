package com.yawarSoft.interoperability.Dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockResponseDTO {
    private String bloodType;
    private String rhFactor;
    private String unitType;
    private Long quantity;
}

