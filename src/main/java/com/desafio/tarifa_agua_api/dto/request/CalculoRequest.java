package com.desafio.tarifa_agua_api.dto.request;


import com.desafio.tarifa_agua_api.entity.CategoriaConsumidor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CalculoRequest(

        @NotNull(message = "A categoria é obrigatória.")
        CategoriaConsumidor categoria,

        @NotNull(message = "O consumo é obrigatório.")
        @PositiveOrZero(message = "O consumo deve ser maior ou igual a zero.")
        Integer consumo
) {
}
