package com.yawarSoft.interoperability.Services.Interfaces;

import com.yawarSoft.interoperability.Dtos.StockResponseDTO;

import java.util.List;

public interface FhirClientService {
    List<StockResponseDTO> getObservationsFromExternalSystemByName(String nombreBanco);
    List<StockResponseDTO> getObservationsFromExternalSystemById(Integer idBloodBank);
}
