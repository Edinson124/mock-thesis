package com.yawarSoft.interoperability.Providers;

import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.yawarSoft.interoperability.Services.Interfaces.BloodBankService;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Organization;
import org.springframework.stereotype.Component;

@Component
public class OrganizationResourceProvider implements IResourceProvider {

    private final BloodBankService bloodBankService;

    public OrganizationResourceProvider(BloodBankService bloodBankService) {
        this.bloodBankService = bloodBankService;
    }

    @Override
    public Class<Organization> getResourceType() {
        return Organization.class;
    }

    @Search
    public Bundle searchActiveInternalBloodBanks(
            @OptionalParam(name = "_page") String pageStr,
            HttpServletRequest request
    ) {
        int page = 1;
        try {
            page = Integer.parseInt(pageStr);
        } catch (NumberFormatException | NullPointerException e) {
            throw new InvalidRequestException("El parámetro page debe ser numérico");
        }

        return bloodBankService.getPagedBloodBanks(10, page, request.getRequestURL().toString());
    }
}
