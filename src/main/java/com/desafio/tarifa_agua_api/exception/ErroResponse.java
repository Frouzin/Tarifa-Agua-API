package com.desafio.tarifa_agua_api.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErroResponse(
        LocalDateTime timestamp,
        Integer status,
        String error,
        List<String> mensagens
) {
}
