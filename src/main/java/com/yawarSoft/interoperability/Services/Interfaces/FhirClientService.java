package com.yawarSoft.interoperability.Services.Interfaces;

import com.yawarSoft.interoperability.Dtos.StockBancoSangreDTO;

import java.util.List;

public interface FhirClientService {
    List<StockBancoSangreDTO> getObservationsFromExternalSystemByName(String nombreBanco);
    List<StockBancoSangreDTO> getObservationsFromExternalSystemById(Integer idBloodBank);
}
