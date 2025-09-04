package com.univille.br.model;

public class Imovel {
    private Integer id;
    private String endereco;
    private String tipo;
    private Double area;
    private Integer quartos;
    private Integer banheiros;
    private Double valorAluguel;
    private Boolean disponivel;

    public Imovel() {}

    public Imovel(Integer id, String endereco, String tipo, Double area,
                  Integer quartos, Integer banheiros, Double valorAluguel, Boolean disponivel) {
        this.id = id;
        this.endereco = endereco;
        this.tipo = tipo;
        this.area = area;
        this.quartos = quartos;
        this.banheiros = banheiros;
        this.valorAluguel = valorAluguel;
        this.disponivel = disponivel;
    }

    public Imovel(String endereco, String tipo, Double area,
                  Integer quartos, Integer banheiros, Double valorAluguel) {
        this(null, endereco, tipo, area, quartos, banheiros, valorAluguel, true);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Double getArea() { return area; }
    public void setArea(Double area) { this.area = area; }

    public Integer getQuartos() { return quartos; }
    public void setQuartos(Integer quartos) { this.quartos = quartos; }

    public Integer getBanheiros() { return banheiros; }
    public void setBanheiros(Integer banheiros) { this.banheiros = banheiros; }

    public Double getValorAluguel() { return valorAluguel; }
    public void setValorAluguel(Double valorAluguel) { this.valorAluguel = valorAluguel; }

    public Boolean getDisponivel() { return disponivel; }
    public void setDisponivel(Boolean disponivel) { this.disponivel = disponivel; }

    @Override
    public String toString() {
        return "Imovel{id=" + id +
                ", endereco='" + endereco + '\'' +
                ", tipo='" + tipo + '\'' +
                ", area=" + area +
                ", quartos=" + quartos +
                ", banheiros=" + banheiros +
                ", valorAluguel=" + valorAluguel +
                ", disponivel=" + disponivel +
                '}';
    }
}
