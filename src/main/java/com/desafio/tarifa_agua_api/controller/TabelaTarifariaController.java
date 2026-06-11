package com.desafio.tarifa_agua_api.controller;

import com.desafio.tarifa_agua_api.dto.request.CriarTabelaTarifariaRequest;
import com.desafio.tarifa_agua_api.dto.response.TabelaTarifariaResponse;
import com.desafio.tarifa_agua_api.service.TabelaTarifariaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tabelas-tarifarias")
public class TabelaTarifariaController {

    private final TabelaTarifariaService tabelaTarifariaService;

    public TabelaTarifariaController(TabelaTarifariaService tabelaTarifariaService) {
        this.tabelaTarifariaService = tabelaTarifariaService;
    }

    @PostMapping
    public ResponseEntity<TabelaTarifariaResponse> criar(
            @Valid @RequestBody CriarTabelaTarifariaRequest request) {
        TabelaTarifariaResponse response = tabelaTarifariaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TabelaTarifariaResponse>> listar() {
        List<TabelaTarifariaResponse> response = tabelaTarifariaService.listar();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        tabelaTarifariaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
