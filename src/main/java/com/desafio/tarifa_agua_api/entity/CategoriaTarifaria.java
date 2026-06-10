package com.desafio.tarifa_agua_api.entity;


import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categoria_tarifaria")
public class CategoriaTarifaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CategoriaConsumidor categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tabela_tarifaria_id", nullable = false)
    private TabelaTarifaria tabelaTarifaria;

    @OneToMany(mappedBy = "categoriaTarifaria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FaixaConsumo> faixas = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public CategoriaConsumidor getCategoria() {
        return categoria;
    }

    public TabelaTarifaria getTabelaTarifaria() {
        return tabelaTarifaria;
    }

    public List<FaixaConsumo> getFaixas() {
        return faixas;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCategoria(CategoriaConsumidor categoria) {
        this.categoria = categoria;
    }

    public void setTabelaTarifaria(TabelaTarifaria tabelaTarifaria) {
        this.tabelaTarifaria = tabelaTarifaria;
    }

    public void setFaixas(List<FaixaConsumo> faixas) {
        this.faixas = faixas;
    }
}
