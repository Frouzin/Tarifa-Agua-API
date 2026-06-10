package com.desafio.tarifa_agua_api.dto.response;

import java.math.BigDecimal;

public record DetalhamentoCalculoResponse(
        FaixaCalculoResponse faixa,
        Integer m3Cobrados,
        BigDecimal valorUnitario,
        BigDecimal subtota
) {
}
