package com.yawarSoft.interoperability.Repositories;

import com.yawarSoft.interoperability.Dtos.StockResumenDTO;
import com.yawarSoft.interoperability.Entities.BloodTypeMock;
import com.yawarSoft.interoperability.Entities.UnitMock;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InMemoryUnitRepositoryMock {

    private final List<UnitMock> units = new ArrayList<>();

    public InMemoryUnitRepositoryMock() {
        // Tipos de sangre de ejemplo
        BloodTypeMock aPos = BloodTypeMock.builder()
                .id("1")
                .tipo("A")
                .rh("POS")
                .build();

        BloodTypeMock oNeg = BloodTypeMock.builder()
                .id("2")
                .tipo("O")
                .rh("NEG")
                .build();

        // Carga inicial de datos
        units.add(UnitMock.builder()
                .id("1")
                .tipo("Concentrado de hematíes")
                .grupoSanguineo(aPos)
                .fechaVencimiento("2025-12-31")
                .estado("ALMACENADO")
                .build());

        units.add(UnitMock.builder()
                .id("2")
                .tipo("Plasma fresco")
                .grupoSanguineo(oNeg)
                .fechaVencimiento("2025-10-15")
                .estado("ALMACENADO")
                .build());

        units.add(UnitMock.builder()
                .id("3")
                .tipo("Concentrado de hematíes")
                .grupoSanguineo(aPos)
                .fechaVencimiento("2025-08-30")
                .estado("VENCIDO")
                .build());
    }

    public List<UnitMock> findAll() {
        return units;
    }

    public List<UnitMock> findAvailable() {
        return units.stream()
                .filter(u -> "ALMACENADO".equals(u.getEstado()))
                .toList();
    }

    public Optional<UnitMock> findById(String id) {
        return units.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    public List<StockResumenDTO> getResumenStock() {
        return units.stream()
                .filter(u -> "ALMACENADO".equals(u.getEstado()))
                .collect(Collectors.groupingBy(u ->
                        u.getGrupoSanguineo().getTipo() + "|" + u.getGrupoSanguineo().getRh() + "|" + u.getTipo()
                ))
                .entrySet()
                .stream()
                .map(entry -> {
                    String[] parts = entry.getKey().split("\\|");
                    String sangre = parts[0];
                    String rh = "+".equals(parts[1]) ? "POS" : "NEG";
                    String tipo = parts[2];
                    long cantidad = entry.getValue().size();
                    return StockResumenDTO.builder()
                            .sangre(sangre)
                            .rh(rh)
                            .tipo(tipo)
                            .cantidad(cantidad)
                            .build();
                })
                .sorted(Comparator.comparing(StockResumenDTO::getSangre)
                        .thenComparing(StockResumenDTO::getTipo))
                .toList();
    }

}

