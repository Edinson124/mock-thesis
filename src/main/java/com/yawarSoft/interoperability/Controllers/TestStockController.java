package com.yawarSoft.interoperability.Controllers;

import com.yawarSoft.interoperability.Dtos.StockResponseDTO;
import com.yawarSoft.interoperability.Services.Interfaces.FhirClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class TestStockController {
    @Autowired
    private FhirClientService fhirClientService;

    @GetMapping("/stock")
    public List<StockResponseDTO> getExternalStock(
            @RequestParam(required = false) Integer idBloodBank,
            @RequestParam(required = false) String nombre) {

        List<StockResponseDTO> stockList;

        if (idBloodBank != null) {
            // Caso 1: Ya tengo el ID del banco
            stockList = fhirClientService.getObservationsFromExternalSystemById(idBloodBank);
        } else if (nombre != null && !nombre.isEmpty()) {
            // Caso 2: Tengo el nombre, primero resuelvo el id
            stockList = fhirClientService.getObservationsFromExternalSystemByName(nombre);
        } else {
            // Ningún parámetro válido
            throw new IllegalArgumentException("Se requiere idBloodBank o nombre para obtener el stock");
        }

        // Ahora hacemos el sout para los que tienen quantity > 0
        stockList.stream()
                .filter(s -> s.getCantidad() != null && s.getCantidad() > 0)
                .forEach(s -> System.out.println(s.toString()));

        return stockList;
    }
}

