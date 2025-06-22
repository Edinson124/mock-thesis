package com.yawarSoft.interoperability.Services.Impelementations;


import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import com.yawarSoft.interoperability.Dtos.AuthLoginRequest;
import com.yawarSoft.interoperability.Dtos.StockResponseDTO;
import com.yawarSoft.interoperability.Services.Interfaces.FhirClientService;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class FhirClientServiceImpl implements FhirClientService {

    private final FhirContext fhirContext;

    public FhirClientServiceImpl(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
    }


    @Override
    public List<StockResponseDTO> getObservationsFromExternalSystemByName(String nombreBanco) {
        String urlBaseExterno = "http://localhost:8081";
        String token = login(urlBaseExterno);
        // 1️⃣ Consultar todos los bancos de sangre
        List<Organization> bancos = obtenerBancosExterno(urlBaseExterno, token);

        // 2️⃣ Filtrar por nombre en memoria
        Organization bancoEncontrado = bancos.stream()
                .filter(org -> org.getName() != null && org.getName().toLowerCase().contains(nombreBanco.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No se encontraron bancos de sangre para: " + nombreBanco));

        String idBloodBank = bancoEncontrado.getIdElement().getIdPart();

        // 3️⃣ Ahora obtener el stock para ese id de banco de sangre
        IGenericClient client = fhirContext.newRestfulGenericClient(urlBaseExterno + "/fhir");
        client.registerInterceptor(new BearerTokenAuthInterceptor(token));

        Bundle bundle = client.search()
                .forResource(Observation.class)
                .where(new ReferenceClientParam("performer").hasId(idBloodBank))
                .returnBundle(Bundle.class)
                .execute();

        // 4️⃣ Convertir y retornar
        return toStockResponseList(bundle);
    }

    @Override
    public List<StockResponseDTO> getObservationsFromExternalSystemById(Integer idBloodBank) {
        String urlBaseExterno = "http://localhost:8081";
        String token = login(urlBaseExterno);

        // Crear cliente FHIR con el token
        IGenericClient client = fhirContext.newRestfulGenericClient(urlBaseExterno + "/fhir");
        client.registerInterceptor(new BearerTokenAuthInterceptor(token));

        // Consultar directamente por id de banco de sangre
        Bundle bundle = client.search()
                .forResource(Observation.class)
                .where(new ReferenceClientParam("performer").hasId(idBloodBank.toString()))
                .returnBundle(Bundle.class)
                .execute();

        // Convertir y retornar
        return toStockResponseList(bundle);
    }

// -------------- MÉTODO PARA OBTENER BANCOS DE SANGRE -------------

    private List<Organization> obtenerBancosExterno(String urlBaseExterno, String token) {
        IGenericClient client = fhirContext.newRestfulGenericClient(urlBaseExterno + "/fhir");
        client.registerInterceptor(new BearerTokenAuthInterceptor(token));

        Bundle bundle = client.search()
                .forResource(Organization.class)
                .returnBundle(Bundle.class)
                .execute();

        return bundle.getEntry()
                .stream()
                .map(entry -> (Organization) entry.getResource())
                .toList();
    }

    public String login(String urlBaseExterno) {
        RestTemplate restTemplate = new RestTemplate();
        String loginUrl = urlBaseExterno + "/client/auth/login";

        AuthLoginRequest request = new AuthLoginRequest("pricing", "Prod_Yawar43");
        ResponseEntity<Map> response = restTemplate.postForEntity(loginUrl, request, Map.class);

        if (response.getBody() != null && response.getBody().get("token") != null) {
            return response.getBody().get("token").toString();
        } else {
            throw new RuntimeException("No se pudo obtener el token de autenticación");
        }
    }

    public List<StockResponseDTO> toStockResponseList(Bundle bundle) {
        return bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource instanceof Observation)
                .map(resource -> (Observation) resource)
                .map(observation -> {
                    String unitType = observation.getCode().getText();
                    Long quantity = observation.getValueQuantity() != null && observation.getValueQuantity().getValue() != null
                            ? observation.getValueQuantity().getValue().longValue()
                            : 0L;

                    String bloodType = null;
                    String rhFactor = null;

                    for (Observation.ObservationComponentComponent component : observation.getComponent()) {
                        if ("Grupo sanguíneo".equals(component.getCode().getText())) {
                            // "Grupo sanguíneo O" -> Queremos "O"
                            String text = component.getValueCodeableConcept().getText();
                            if (text != null && text.startsWith("Grupo sanguíneo ")) {
                                bloodType = text.replace("Grupo sanguíneo ", "");
                            } else {
                                bloodType = text;
                            }
                        } else if ("Factor Rh".equals(component.getCode().getText())) {
                            // "Rh positivo" -> Queremos "POS"; "Rh negativo" -> "NEG"
                            String display = component.getValueCodeableConcept().getCodingFirstRep().getDisplay();
                            if ("Rh positivo".equals(display)) {
                                rhFactor = "POSITIVO";
                            } else if ("Rh negativo".equals(display)) {
                                rhFactor = "NEGATIVO";
                            } else {
                                rhFactor = display;
                            }
                        }
                    }

                    return StockResponseDTO.builder()
                            .grupoSanguineo(bloodType)
                            .rh(rhFactor)
                            .tipoUnidad(unitType)
                            .cantidad(quantity)
                            .build();
                })
                .toList();
    }

}

