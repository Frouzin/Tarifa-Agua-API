package com.desafio.tarifa_agua_api.dto.response;

import java.math.BigDecimal;

public record FaixaConsumoResponse(

        Long id,
        Integer inicio,
        Integer fim,
        BigDecimal valorUnitario
) {
}
