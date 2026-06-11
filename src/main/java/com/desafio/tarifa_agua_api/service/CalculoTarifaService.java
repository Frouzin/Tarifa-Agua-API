package com.desafio.tarifa_agua_api.service;

import com.desafio.tarifa_agua_api.dto.request.CalculoRequest;
import com.desafio.tarifa_agua_api.dto.response.CalculoResponse;
import com.desafio.tarifa_agua_api.dto.response.DetalhamentoCalculoResponse;
import com.desafio.tarifa_agua_api.dto.response.FaixaCalculoResponse;
import com.desafio.tarifa_agua_api.entity.CategoriaTarifaria;
import com.desafio.tarifa_agua_api.entity.FaixaConsumo;
import com.desafio.tarifa_agua_api.entity.TabelaTarifaria;
import com.desafio.tarifa_agua_api.exception.BusinessException;
import com.desafio.tarifa_agua_api.repository.TabelaTarifariaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CalculoTarifaService {

    private final TabelaTarifariaRepository tabelaTarifariaRepository;

    public CalculoTarifaService(TabelaTarifariaRepository tabelaTarifariaRepository) {
        this.tabelaTarifariaRepository = tabelaTarifariaRepository;
    }

    @Transactional(readOnly = true)
    public CalculoResponse calcular(CalculoRequest request) {
        TabelaTarifaria tabela = tabelaTarifariaRepository.findTopByAtivaTrueOrderByDataVigenciaDescIdDesc()
                .orElseThrow(() -> new BusinessException("Nenhuma tabela tarifária ativa encontrada."));

        CategoriaTarifaria categoriaTarifaria = tabela.getCategorias()
                .stream()
                .filter(categoria -> categoria.getCategoria().equals(request.categoria()))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Categoria não encontrada na tabela tarifária ativa."));

        List<FaixaConsumo> faixas = categoriaTarifaria.getFaixas()
                .stream()
                .sorted(Comparator.comparing(FaixaConsumo::getInicio))
                .toList();

        validarCoberturaConsumo(faixas, request.consumo());

        BigDecimal valorTotal = BigDecimal.ZERO;
        List<DetalhamentoCalculoResponse> detalhamento = new ArrayList<>();

        for (FaixaConsumo faixa : faixas) {
            if (request.consumo() <= faixa.getInicio()) {
                if (faixa.getInicio() != 0) {
                    break;
                }
            }

            if (request.consumo() < faixa.getInicio()) {
                break;
            }

            int limiteSuperiorAplicavel = Math.min(request.consumo(), faixa.getFim());

            int m3Cobrados;

            if (faixa.getInicio() == 0) {
                m3Cobrados = limiteSuperiorAplicavel - faixa.getInicio();
            } else {
                m3Cobrados = limiteSuperiorAplicavel - faixa.getInicio() + 1;
            }

            if (m3Cobrados <= 0) {
                continue;
            }

            BigDecimal subtotal = faixa.getValorUnitario()
                    .multiply(BigDecimal.valueOf(m3Cobrados));

            valorTotal = valorTotal.add(subtotal);

            detalhamento.add(new DetalhamentoCalculoResponse(
                    new FaixaCalculoResponse(faixa.getInicio(), faixa.getFim()),
                    m3Cobrados,
                    faixa.getValorUnitario(),
                    subtotal
            ));
        }

        return new CalculoResponse(
                request.categoria(),
                request.consumo(),
                valorTotal,
                detalhamento
        );
    }

    private void validarCoberturaConsumo(List<FaixaConsumo> faixas, Integer consumo) {
        boolean consumoCoberto = faixas.stream()
                .anyMatch(faixa -> consumo >= faixa.getInicio() && consumo <= faixa.getFim());

        if (!consumoCoberto) {
            throw new BusinessException("Não há faixa cadastrada para cobrir o consumo informado.");
        }
    }
}
