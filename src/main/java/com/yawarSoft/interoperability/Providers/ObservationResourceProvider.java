package com.yawarSoft.interoperability.Providers;


import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.yawarSoft.interoperability.Services.Interfaces.UnitService;
import org.hl7.fhir.r4.model.Observation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ObservationResourceProvider implements IResourceProvider {

    private final UnitService unitService;

    public ObservationResourceProvider(UnitService unitService) {
        this.unitService = unitService;
    }

    @Override
    public Class<Observation> getResourceType() {
        return Observation.class;
    }

    @Search
    public List<Observation> getStockByBloodBank(@RequiredParam(name = "performer") StringParam bancoIdParam) {
        String input = bancoIdParam.getValue(); // Puede ser "2" o "Organization/2"
        String idSolo;

        if (input.matches("\\d+")) {
            // Es solo el ID numérico
            idSolo = input;
        } else if (input.matches("Organization/\\d+")) {
            // Es el formato Organization/2
            idSolo = input.split("/")[1];
        } else {
            throw new InvalidRequestException("El parámetro performer debe ser un ID numérico o 'Organization/{id}'");
        }
        return unitService.getStockByBloodBank(idSolo);
    }
}
