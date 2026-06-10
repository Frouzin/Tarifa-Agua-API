package com.desafio.tarifa_agua_api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tabela_tarifaria")
public class TabelaTarifaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private LocalDate dataVigencia;

    private Boolean ativa = true;


    @OneToMany(mappedBy = "tabelaTarifaria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoriaTarifaria> categorias = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataVigencia() {
        return dataVigencia;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public List<CategoriaTarifaria> getCategorias() {
        return categorias;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDataVigencia(LocalDate dataVigencia) {
        this.dataVigencia = dataVigencia;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public void setCategorias(List<CategoriaTarifaria> categorias) {
        this.categorias = categorias;
    }
}
