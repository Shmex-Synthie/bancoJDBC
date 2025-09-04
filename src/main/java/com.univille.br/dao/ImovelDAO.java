package com.univille.br.dao;

import com.univille.br.DB;
import com.univille.br.model.Imovel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImovelDAO {

    public void inserir(Imovel i) throws SQLException {
        String sql = "INSERT INTO imovel (endereco, tipo, area, quartos, banheiros, valor_aluguel, disponivel) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, i.getEndereco());
            ps.setString(2, i.getTipo());
            if (i.getArea() != null) ps.setDouble(3, i.getArea()); else ps.setNull(3, Types.DOUBLE);
            if (i.getQuartos() != null) ps.setInt(4, i.getQuartos()); else ps.setNull(4, Types.INTEGER);
            if (i.getBanheiros() != null) ps.setInt(5, i.getBanheiros()); else ps.setNull(5, Types.INTEGER);
            if (i.getValorAluguel() != null) ps.setDouble(6, i.getValorAluguel()); else ps.setNull(6, Types.DECIMAL);
            ps.setBoolean(7, i.getDisponivel() != null ? i.getDisponivel() : true);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) i.setId(rs.getInt(1));
            }
        }
    }

    public Imovel buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM imovel WHERE id_imovel = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        }
        return null;
    }

    public List<Imovel> listarDisponiveis() throws SQLException {
        List<Imovel> lista = new ArrayList<>();
        String sql = "SELECT * FROM imovel WHERE disponivel = TRUE ORDER BY endereco";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(map(rs));
            }
        }
        return lista;
    }

    public void atualizarDisponibilidade(int idImovel, boolean disponivel) throws SQLException {
        String sql = "UPDATE imovel SET disponivel = ? WHERE id_imovel = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, disponivel);
            ps.setInt(2, idImovel);
            ps.executeUpdate();
        }
    }

    private Imovel map(ResultSet rs) throws SQLException {
        Imovel i = new Imovel();
        i.setId(rs.getInt("id_imovel"));
        i.setEndereco(rs.getString("endereco"));
        i.setTipo(rs.getString("tipo"));
        double area = rs.getDouble("area"); i.setArea(rs.wasNull() ? null : area);
        int quartos = rs.getInt("quartos"); i.setQuartos(rs.wasNull() ? null : quartos);
        int banheiros = rs.getInt("banheiros"); i.setBanheiros(rs.wasNull() ? null : banheiros);
        double valor = rs.getDouble("valor_aluguel"); i.setValorAluguel(rs.wasNull() ? null : valor);
        i.setDisponivel(rs.getBoolean("disponivel"));
        return i;
    }
}
