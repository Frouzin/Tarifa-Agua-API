package com.desafio.tarifa_agua_api.repository;

import com.desafio.tarifa_agua_api.entity.CategoriaTarifaria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaTarifariaRepository extends JpaRepository<CategoriaTarifaria, Long> {
}
