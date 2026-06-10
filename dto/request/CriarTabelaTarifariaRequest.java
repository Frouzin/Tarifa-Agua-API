package com.desafio.tarifa_agua_api.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record CriarTabelaTarifariaRequest(

        @NotBlank(message = "O nome da tabela tarifária é obrigatório.")
        String nome,

        @NotNull(message = "A data de vigência é obrigatória.")
        LocalDate dataVigencia,

        @Valid
        @NotEmpty(message = "A tabela tarifária deve possuir pelo menos uma categoria.")
        List<CategoriaTarifariaRequest> categorias

) {
}
