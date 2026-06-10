package com.desafio.tarifa_agua_api.dto.response;

import com.desafio.tarifa_agua_api.entity.CategoriaConsumidor;
import java.math.BigDecimal;
import java.util.List;

public record CalculoResponse(
        CategoriaConsumidor categoria,
        Integer consumoTotal,
        BigDecimal valorTotal,
        List<DetalhamentoCalculoResponse> detalhamento
) {
}
