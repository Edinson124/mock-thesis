package com.yawarSoft.interoperability.Config;


import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class FhirServerConfig {

    private final List<IResourceProvider> resourceProviders;
    private final FhirContext fhirContext;

    public FhirServerConfig(List<IResourceProvider> resourceProviders, FhirContext fhirContext) {
        this.resourceProviders = resourceProviders;
        this.fhirContext = fhirContext;
    }

    @Bean
    public ServletRegistrationBean<jakarta.servlet.Servlet> fhirServlet() {
        RestfulServer restfulServer = new RestfulServer(fhirContext);

        restfulServer.setResourceProviders(resourceProviders);
        restfulServer.setDefaultResponseEncoding(EncodingEnum.JSON);
        restfulServer.setDefaultPrettyPrint(true);

        return new ServletRegistrationBean<>(restfulServer, "/fhir/*");
    }

}
