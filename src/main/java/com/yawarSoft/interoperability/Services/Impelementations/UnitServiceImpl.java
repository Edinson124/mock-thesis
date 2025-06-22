package com.yawarSoft.interoperability.Services.Impelementations;

import com.yawarSoft.interoperability.Dtos.StockResumenDTO;
import com.yawarSoft.interoperability.Enums.BloodSNOMED;
import com.yawarSoft.interoperability.Enums.UnitTypes;
import com.yawarSoft.interoperability.Repositories.InMemoryUnitRepositoryMock;
import com.yawarSoft.interoperability.Services.Interfaces.AuthenticatedExternalClientService;
import com.yawarSoft.interoperability.Services.Interfaces.UnitService;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UnitServiceImpl implements UnitService {

    private final InMemoryUnitRepositoryMock unitRepositoryMock;
    private final AuthenticatedExternalClientService authenticatedExternalClientService;

    public UnitServiceImpl(InMemoryUnitRepositoryMock unitRepositoryMock, AuthenticatedExternalClientService authenticatedExternalClientService) {
        this.unitRepositoryMock = unitRepositoryMock;
        this.authenticatedExternalClientService = authenticatedExternalClientService;
    }


    @Override
    public Bundle getStockByBloodBank() {
        List<StockResumenDTO> resumenDB = unitRepositoryMock.getResumenStock();
        List<Observation> observations = buildObservations(resumenDB);

        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.SEARCHSET);
        for (Observation obs : observations) {
            bundle.addEntry()
                    .setFullUrl("urn:uuid:" + obs.getId())
                    .setResource(obs);
        }
        return bundle;
    }

    private List<Observation> buildObservations(List<StockResumenDTO> resumenDB) {
        // Listas estándar
        List<String> bloodTypes = List.of("O|POS", "O|NEG", "A|POS", "A|NEG", "B|POS", "B|NEG", "AB|POS", "AB|NEG");
        List<String> unitTypes = Arrays.stream(UnitTypes.values())
                .map(UnitTypes::getLabel)
                .toList();

        // Crear mapa para búsqueda rápida
        Map<String, StockResumenDTO> resumenMap = resumenDB.stream()
                .collect(Collectors.toMap(d -> d.getSangre() + "|" + d.getRh() + "|" + d.getTipo(), d -> d));

        // Crear listado final para todas las combinaciones
        List<StockResumenDTO> resumenFinal = new ArrayList<>();
        for (String blood : bloodTypes) {
            for (String unit : unitTypes) {
                String[] bloodParts = blood.split("\\|");
                String sangre = bloodParts[0];
                String rh = bloodParts[1];
                String key = sangre + "|" + rh + "|" + unit;

                resumenFinal.add(
                        resumenMap.getOrDefault(key,
                                StockResumenDTO.builder()
                                        .sangre(sangre)
                                        .rh(rh)
                                        .tipo(unit)
                                        .cantidad(0L)
                                        .build())
                );
            }
        }

        // Ahora construir las Observation
        List<Observation> observations = new ArrayList<>();
        for (StockResumenDTO stock : resumenFinal) {
            Observation obs = new Observation();
            obs.setId(IdType.newRandomUuid());
            obs.setStatus(Observation.ObservationStatus.FINAL);
            obs.setCode(new CodeableConcept().setText(stock.getTipo()));
            obs.setValue(new Quantity().setValue(stock.getCantidad()).setUnit("unidades"));

            BloodSNOMED bloodSNOMED = BloodSNOMED.fromLabel(stock.getSangre());

            // Grupo sanguíneo
            CodeableConcept bloodGroupConcept = new CodeableConcept()
                    .addCoding(new Coding()
                            .setSystem("http://snomed.info/sct")
                            .setCode(bloodSNOMED.getSnomedCode())
                            .setDisplay(bloodSNOMED.getDisplay()))
                    .setText("Grupo sanguíneo " + stock.getSangre());

            // Factor Rh
            boolean isRhPositive = "POS".equals(stock.getRh());
            CodeableConcept rhConcept = new CodeableConcept()
                    .addCoding(new Coding()
                            .setSystem("http://snomed.info/sct")
                            .setCode(isRhPositive ? "165747007" : "165746003")
                            .setDisplay(isRhPositive ? "Rh positivo" : "Rh negativo"))
                    .setText("Factor Rh");

            obs.addComponent(new Observation.ObservationComponentComponent()
                    .setCode(new CodeableConcept().setText("Grupo sanguíneo"))
                    .setValue(bloodGroupConcept));

            obs.addComponent(new Observation.ObservationComponentComponent()
                    .setCode(new CodeableConcept().setText("Factor Rh"))
                    .setValue(rhConcept));

            observations.add(obs);
        }

        return observations;
    }

}
