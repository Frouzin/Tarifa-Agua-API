package com.desafio.tarifa_agua_api.repository;

import com.desafio.tarifa_agua_api.entity.TabelaTarifaria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TabelaTarifariaRepository extends JpaRepository<TabelaTarifaria, Long> {
    List<TabelaTarifaria> findAllByAtivaTrue();
    Optional<TabelaTarifaria> findByIdAndAtivaTrue(Long id);
    Optional<TabelaTarifaria> findTopByAtivaTrueOrderByDataVigenciaDescIdDesc();
}
