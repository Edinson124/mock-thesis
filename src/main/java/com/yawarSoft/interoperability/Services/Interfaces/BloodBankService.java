package com.yawarSoft.interoperability.Services.Interfaces;

import org.hl7.fhir.r4.model.Bundle;

public interface BloodBankService {
    Bundle getPagedBloodBanks(int count, int page, String baseUrl);
}
