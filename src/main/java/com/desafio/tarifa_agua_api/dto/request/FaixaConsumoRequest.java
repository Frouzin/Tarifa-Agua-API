package com.desafio.tarifa_agua_api.dto.request;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record FaixaConsumoRequest(

        @NotNull(message = "O início da faixa é obrigatório.")
        Integer inicio,

        @NotNull(message = "O fim da faixa é obrigatório.")
        Integer fim,

        @NotNull(message = "O valor unitário é obrigatório.")
        @DecimalMin(value = "0.00", message = "O valor unitário não pode ser negativo.")
        BigDecimal valorUnitario

) {
}
