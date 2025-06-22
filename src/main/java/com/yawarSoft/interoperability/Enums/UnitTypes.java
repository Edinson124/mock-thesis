package com.yawarSoft.interoperability.Enums;

import lombok.Getter;

@Getter
public enum UnitTypes {
    SANGRE_TOTAL("Sangre completa"),
    CONCENTRADO_ERITROCITOS("Concentrado de hematíes"),
    PLASMA_FRESCO_CONGELADO("Plasma fresco"),
    CRIOPRECIPITADOS("Crioprecipitados"),
    PLAQUETAS("Plaquetas"),
    AFERESIS_PLAQUETAS("Aféresis de plaquetaria"),
    AFERESIS_GLOBULOS_ROJOS("Aféresis de glóbulos rojos"),
    AFERESIS_PLASMA("Aféresis de plasma"),;

    private final String label;

    UnitTypes(String label) {
        this.label = label;
    }

}

