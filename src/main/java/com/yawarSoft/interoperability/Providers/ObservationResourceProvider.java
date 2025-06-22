package com.yawarSoft.interoperability.Providers;


import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import com.yawarSoft.interoperability.Services.Interfaces.UnitService;
import org.hl7.fhir.r4.model.Bundle;
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
    public Bundle getStockByBloodBank() {
        System.out.println("getStockByBloodBank Controller API");
        return unitService.getStockByBloodBank();
    }
}
