package com.desafio.tarifa_agua_api.dto.request;

import com.desafio.tarifa_agua_api.entity.CategoriaConsumidor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CategoriaTarifariaRequest(

        @NotNull(message = "A categoria é obrigatória.")
        CategoriaConsumidor categoria,

        @Valid
        @NotEmpty(message = "A categoria deve possuir pelo menos uma faixa de consumo.")
        List<FaixaConsumoRequest> faixas
) {
}
