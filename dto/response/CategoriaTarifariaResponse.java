package com.desafio.tarifa_agua_api.dto.response;


import com.desafio.tarifa_agua_api.entity.CategoriaConsumidor;
import java.util.List;

public record CategoriaTarifariaResponse(
        Long id,
        CategoriaConsumidor categoria,
        List<FaixaConsumoResponse> faixas
) {
}
