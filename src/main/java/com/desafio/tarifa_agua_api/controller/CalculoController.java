package com.desafio.tarifa_agua_api.controller;

import com.desafio.tarifa_agua_api.dto.request.CalculoRequest;
import com.desafio.tarifa_agua_api.dto.response.CalculoResponse;
import com.desafio.tarifa_agua_api.service.CalculoTarifaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calculos")
public class CalculoController {

    private final CalculoTarifaService calculoTarifaService;

    public CalculoController(CalculoTarifaService calculoTarifaService) {
        this.calculoTarifaService = calculoTarifaService;
    }

    @PostMapping
    public ResponseEntity<CalculoResponse> calcularTarifa(@Valid @RequestBody CalculoRequest request) {
        CalculoResponse response = calculoTarifaService.calcular(request);
        return ResponseEntity.ok(response);
    }
}
