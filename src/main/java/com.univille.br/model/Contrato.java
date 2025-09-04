package com.univille.br.model;

import java.time.LocalDate;

public class Contrato {
    private Integer id;
    private Integer idCliente;
    private Integer idImovel;
    private Double valor;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public Contrato() {}

    public Contrato(Integer id, Integer idCliente, Integer idImovel, Double valor,
                    LocalDate dataInicio, LocalDate dataFim) {
        this.id = id;
        this.idCliente = idCliente;
        this.idImovel = idImovel;
        this.valor = valor;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public Contrato(Integer idCliente, Integer idImovel, Double valor,
                    LocalDate dataInicio, LocalDate dataFim) {
        this(null, idCliente, idImovel, valor, dataInicio, dataFim);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public Integer getIdImovel() { return idImovel; }
    public void setIdImovel(Integer idImovel) { this.idImovel = idImovel; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    @Override
    public String toString() {
        return "Contrato{id=" + id +
                ", idCliente=" + idCliente +
                ", idImovel=" + idImovel +
                ", valor=" + valor +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                '}';
    }
}
