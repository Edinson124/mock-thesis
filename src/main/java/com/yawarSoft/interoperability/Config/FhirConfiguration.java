package com.yawarSoft.interoperability.Config;

import ca.uhn.fhir.context.FhirContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FhirConfiguration {

    @Bean
    public FhirContext fhirContext() {
        return FhirContext.forR4();
    }
}