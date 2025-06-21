package com.yawarSoft.interoperability.Enums;

import lombok.Getter;

@Getter
public enum UnitTypes {
    SANGRE_TOTAL("Sangre total"),
    CONCENTRADO_ERITROCITOS("Concentrado de eritrocitos"),
    PLASMA_FRESCO_CONGELADO("Plasma fresco congelado"),
    CRIOPRECIPITADOS("Crioprecipitados"),
    PLAQUETAS("Plaquetas"),
    AFERESIS_PLAQUETAS("Aféresis de plaquetas"),
    AFERESIS_GLOBULOS_ROJOS("Aféresis de glóbulos rojos"),
    AFERESIS_PLASMA("Aféresis de plasma"),;

    private final String label;

    UnitTypes(String label) {
        this.label = label;
    }

}

