package com.desafio.tarifa_agua_api.entity;


import jakarta.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "faixa_consumo")
public class FaixaConsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer inicio;

    private Integer fim;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorUnitario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_tarifaria_id", nullable = false)
    private CategoriaTarifaria categoriaTarifaria;

    public Long getId() {
        return id;
    }

    public Integer getInicio() {
        return inicio;
    }

    public Integer getFim() {
        return fim;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public CategoriaTarifaria getCategoriaTarifaria() {
        return categoriaTarifaria;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setInicio(Integer inicio) {
        this.inicio = inicio;
    }

    public void setFim(Integer fim) {
        this.fim = fim;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public void setCategoriaTarifaria(CategoriaTarifaria categoriaTarifaria) {
        this.categoriaTarifaria = categoriaTarifaria;
    }
}
