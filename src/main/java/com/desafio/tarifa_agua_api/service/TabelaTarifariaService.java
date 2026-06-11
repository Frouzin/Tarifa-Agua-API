package com.desafio.tarifa_agua_api.service;

import com.desafio.tarifa_agua_api.dto.request.CategoriaTarifariaRequest;
import com.desafio.tarifa_agua_api.dto.request.CriarTabelaTarifariaRequest;
import com.desafio.tarifa_agua_api.dto.request.FaixaConsumoRequest;
import com.desafio.tarifa_agua_api.dto.response.CategoriaTarifariaResponse;
import com.desafio.tarifa_agua_api.dto.response.FaixaConsumoResponse;
import com.desafio.tarifa_agua_api.dto.response.TabelaTarifariaResponse;
import com.desafio.tarifa_agua_api.entity.CategoriaConsumidor;
import com.desafio.tarifa_agua_api.entity.CategoriaTarifaria;
import com.desafio.tarifa_agua_api.entity.FaixaConsumo;
import com.desafio.tarifa_agua_api.entity.TabelaTarifaria;
import com.desafio.tarifa_agua_api.exception.BusinessException;
import com.desafio.tarifa_agua_api.repository.TabelaTarifariaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TabelaTarifariaService {

    private static final int LIMITE_MAXIMO_SUFICIENTE = 99999;


    private final TabelaTarifariaRepository tabelaTarifariaRepository;

    public TabelaTarifariaService(TabelaTarifariaRepository tabelaTarifariaRepository) {
        this.tabelaTarifariaRepository = tabelaTarifariaRepository;
    }

    @Transactional
    public TabelaTarifariaResponse criar(CriarTabelaTarifariaRequest request) {
        validarCategorias(request.categorias());

        TabelaTarifaria tabela = new TabelaTarifaria();
        tabela.setNome(request.nome());
        tabela.setDataVigencia(request.dataVigencia());
        tabela.setAtiva(true);

        for (CategoriaTarifariaRequest categoriaRequest : request.categorias()) {
            validarFaixas(categoriaRequest.faixas(), categoriaRequest.categoria());

            CategoriaTarifaria categoriaTarifaria = new CategoriaTarifaria();
            categoriaTarifaria.setCategoria(categoriaRequest.categoria());
            categoriaTarifaria.setTabelaTarifaria(tabela);

            List<FaixaConsumoRequest> faixasOrdenadas = categoriaRequest.faixas()
                    .stream()
                    .sorted(Comparator.comparing(FaixaConsumoRequest::inicio))
                    .toList();

            for (FaixaConsumoRequest faixaRequest : faixasOrdenadas) {
                FaixaConsumo faixa = new FaixaConsumo();
                faixa.setInicio(faixaRequest.inicio());
                faixa.setFim(faixaRequest.fim());
                faixa.setValorUnitario(faixaRequest.valorUnitario());
                faixa.setCategoriaTarifaria(categoriaTarifaria);

                categoriaTarifaria.getFaixas().add(faixa);
            }

            tabela.getCategorias().add(categoriaTarifaria);
        }

        TabelaTarifaria tabelaSalva = tabelaTarifariaRepository.save(tabela);

        return toResponse(tabelaSalva);
    }

    @Transactional(readOnly = true)
    public List<TabelaTarifariaResponse> listar() {
        return tabelaTarifariaRepository.findAllByAtivaTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void excluir(Long id) {
        TabelaTarifaria tabela = tabelaTarifariaRepository.findByIdAndAtivaTrue(id)
                .orElseThrow(() -> new BusinessException("Tabela tarifária não encontrada ou já inativa."));

        tabela.setAtiva(false);

        tabelaTarifariaRepository.save(tabela);
    }

    private void validarCategorias(List<CategoriaTarifariaRequest> categorias) {
        Set<CategoriaConsumidor> categoriasUnicas = new HashSet<>();

        for (CategoriaTarifariaRequest categoria : categorias) {
            if (!categoriasUnicas.add(categoria.categoria())) {
                throw new BusinessException("Categoria duplicada na tabela tarifária: " + categoria.categoria());
            }
        }
    }

    private void validarFaixas(List<FaixaConsumoRequest> faixas, CategoriaConsumidor categoria) {
        List<FaixaConsumoRequest> ordenadas = faixas.stream()
                .sorted(Comparator.comparing(FaixaConsumoRequest::inicio))
                .toList();

        if (!ordenadas.get(0).inicio().equals(0)) {
            throw new BusinessException("A primeira faixa da categoria " + categoria + " deve iniciar em 0.");
        }

        for (int i = 0; i < ordenadas.size(); i++) {
            FaixaConsumoRequest faixaAtual = ordenadas.get(i);

            if (faixaAtual.inicio() >= faixaAtual.fim()) {
                throw new BusinessException("O início da faixa deve ser menor que o fim na categoria " + categoria + ".");
            }

            if (i > 0) {
                FaixaConsumoRequest faixaAnterior = ordenadas.get(i - 1);

                if (!faixaAtual.inicio().equals(faixaAnterior.fim() + 1)) {
                    throw new BusinessException(
                            "As faixas da categoria " + categoria + " devem ser contínuas, sem lacunas ou sobreposição."
                    );
                }
            }
        }

        FaixaConsumoRequest ultimaFaixa = ordenadas.get(ordenadas.size() - 1);

        if (ultimaFaixa.fim() < LIMITE_MAXIMO_SUFICIENTE) {
            throw new BusinessException(
                    "A última faixa da categoria " + categoria + " deve cobrir consumos até pelo menos "
                            + LIMITE_MAXIMO_SUFICIENTE + " m³."
            );
        }
    }

    private TabelaTarifariaResponse toResponse(TabelaTarifaria tabela) {
        List<CategoriaTarifariaResponse> categorias = tabela.getCategorias()
                .stream()
                .map(this::toCategoriaResponse)
                .toList();

        return new TabelaTarifariaResponse(
                tabela.getId(),
                tabela.getNome(),
                tabela.getDataVigencia(),
                tabela.getAtiva(),
                categorias
        );
    }

    private CategoriaTarifariaResponse toCategoriaResponse(CategoriaTarifaria categoriaTarifaria) {
        List<FaixaConsumoResponse> faixas = categoriaTarifaria.getFaixas()
                .stream()
                .sorted(Comparator.comparing(FaixaConsumo::getInicio))
                .map(this::toFaixaResponse)
                .toList();

        return new CategoriaTarifariaResponse(
                categoriaTarifaria.getId(),
                categoriaTarifaria.getCategoria(),
                faixas
        );
    }

    private FaixaConsumoResponse toFaixaResponse(FaixaConsumo faixa) {
        return new FaixaConsumoResponse(
                faixa.getId(),
                faixa.getInicio(),
                faixa.getFim(),
                faixa.getValorUnitario()
        );
    }
}
