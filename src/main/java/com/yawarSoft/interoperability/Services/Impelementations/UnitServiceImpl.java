package com.yawarSoft.interoperability.Services.Impelementations;

import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.yawarSoft.interoperability.Dtos.StockResumenDTO;
import com.yawarSoft.interoperability.Entities.AuthExternalSystemEntity;
import com.yawarSoft.interoperability.Entities.BloodBankEntity;
import com.yawarSoft.interoperability.Enums.BloodSNOMED;
import com.yawarSoft.interoperability.Enums.NetworkBBStatus;
import com.yawarSoft.interoperability.Enums.UnitTypes;
import com.yawarSoft.interoperability.Repositories.BloodBankNetworkRepository;
import com.yawarSoft.interoperability.Repositories.BloodBankRepository;
import com.yawarSoft.interoperability.Repositories.UnitRepository;
import com.yawarSoft.interoperability.Services.Interfaces.AuthenticatedExternalClientService;
import com.yawarSoft.interoperability.Services.Interfaces.UnitService;
import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;
    private final BloodBankRepository bloodBankRepository;
    private final AuthenticatedExternalClientService authenticatedExternalClientService;
    private final BloodBankNetworkRepository bloodBankNetworkRepository;

    public UnitServiceImpl(UnitRepository unitRepository, BloodBankRepository bloodBankRepository, AuthenticatedExternalClientService authenticatedExternalClientService, BloodBankNetworkRepository bloodBankNetworkRepository) {
        this.unitRepository = unitRepository;
        this.bloodBankRepository = bloodBankRepository;
        this.authenticatedExternalClientService = authenticatedExternalClientService;
        this.bloodBankNetworkRepository = bloodBankNetworkRepository;
    }


    @Override
    public List<Observation> getStockByBloodBank(String bloodBankId) {
        AuthExternalSystemEntity authExternalSystem = authenticatedExternalClientService.getExternalClient();
        Integer idBloodBankAuth = authExternalSystem.getBloodBank().getId();

        Integer id = Integer.valueOf(bloodBankId);

        BloodBankEntity banco = bloodBankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Banco de sangre no encontrado"));
        String nombreBanco = banco.getName();

        long relations = bloodBankNetworkRepository.countActiveRelationsBetweenBanks(
                idBloodBankAuth, id, NetworkBBStatus.ACTIVE.name());

        if (relations == 0) {
            throw new ForbiddenOperationException("No pertenece a una misma red.");
        }


        List<StockResumenDTO> resumenDB = unitRepository.getResumenStockByBloodBank(id);
        return buildObservations(resumenDB, id, nombreBanco);
    }

    private List<Observation> buildObservations(List<StockResumenDTO> resumenDB, Integer id, String nombreBanco) {
        List<String> bloodTypes = List.of("O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-");
        List<String> unitTypes = Arrays.stream(UnitTypes.values())
                .map(UnitTypes::getLabel)
                .toList();

        // Crear mapa para búsqueda rápida
        Map<String, StockResumenDTO> resumenMap = new HashMap<>();
        for (StockResumenDTO dto : resumenDB) {
            String key = dto.getBloodType() + "|" + dto.getUnitType();
            resumenMap.put(key, dto);
        }

        // Crear listado final para todas las combinaciones
        List<StockResumenDTO> resumenFinal = new ArrayList<>();
        for (String blood : bloodTypes) {
            for (String unit : unitTypes) {
                String key = blood + "|" + unit;
                if (resumenMap.containsKey(key)) {
                    resumenFinal.add(resumenMap.get(key));
                } else {
                    resumenFinal.add(new StockResumenDTO(blood, unit, 0L));
                }
            }
        }

        // Ahora construir las Observation
        List<Observation> observations = new ArrayList<>();

        for (StockResumenDTO stock : resumenFinal) {
            Observation obs = new Observation();
            obs.setId(IdType.newRandomUuid());
            obs.setStatus(Observation.ObservationStatus.FINAL);
            obs.setCode(new CodeableConcept().setText(stock.getUnitType()));
            obs.setValue(new Quantity().setValue(stock.getQuantity()).setUnit("unidades"));

            String fullType = stock.getBloodType();
            String bloodGroup = fullType.replace("+", "").replace("-", "");
            boolean isRhPositive = fullType.contains("+");

            BloodSNOMED bloodSNOMED = BloodSNOMED.fromLabel(bloodGroup);

            CodeableConcept bloodGroupConcept = new CodeableConcept()
                    .addCoding(new Coding()
                            .setSystem("http://snomed.info/sct")
                            .setCode(bloodSNOMED.getSnomedCode())
                            .setDisplay(bloodSNOMED.getDisplay()))
                    .setText("Grupo sanguíneo " + bloodGroup);

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

            obs.addPerformer(new Reference("Organization/" + id).setDisplay(nombreBanco));

            observations.add(obs);
        }

        return observations;
    }


}
