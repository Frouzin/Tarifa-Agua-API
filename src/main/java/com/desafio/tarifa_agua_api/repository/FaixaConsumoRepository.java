package com.desafio.tarifa_agua_api.repository;

import com.desafio.tarifa_agua_api.entity.FaixaConsumo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaixaConsumoRepository extends JpaRepository<FaixaConsumo, Long> {
}
