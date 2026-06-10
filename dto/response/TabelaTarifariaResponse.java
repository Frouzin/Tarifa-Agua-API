package com.desafio.tarifa_agua_api.dto.response;


import java.time.LocalDate;
import java.util.List;

public record TabelaTarifariaResponse(
        Long id,
        String nome,
        LocalDate dataVigencia,
        Boolean ativa,
        List<CategoriaTarifariaResponse> categorias
) {
}
