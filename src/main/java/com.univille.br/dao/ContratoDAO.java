package com.univille.br.dao;

import com.univille.br.DB;
import com.univille.br.model.Contrato;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContratoDAO {

    public void inserir(Contrato ct) throws SQLException {
        // Regra: ao criar contrato, marcar o imóvel como indisponível
        String sql = "INSERT INTO contrato (id_cliente, id_imovel, valor, data_inicio, data_fim) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DB.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, ct.getIdCliente());
                ps.setInt(2, ct.getIdImovel());
                ps.setDouble(3, ct.getValor());
                ps.setDate(4, Date.valueOf(ct.getDataInicio()));
                ps.setDate(5, Date.valueOf(ct.getDataFim()));
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) ct.setId(rs.getInt(1));
                }
            }
            try (PreparedStatement upd = conn.prepareStatement(
                    "UPDATE imovel SET disponivel = FALSE WHERE id_imovel = ?")) {
                upd.setInt(1, ct.getIdImovel());
                upd.executeUpdate();
            }
            conn.commit();
        }
    }

    public List<Contrato> listarAtivos() throws SQLException {
        List<Contrato> lista = new ArrayList<>();
        String sql = "SELECT * FROM contrato WHERE CURDATE() BETWEEN data_inicio AND data_fim ORDER BY data_fim";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        }
        return lista;
    }

    public List<Contrato> listarExpirandoEm30Dias() throws SQLException {
        List<Contrato> lista = new ArrayList<>();
        String sql = "SELECT * FROM contrato " +
                "WHERE data_fim BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY) " +
                "ORDER BY data_fim";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(map(rs));
        }
        return lista;
    }

    public List<ClienteComContagem> clientesComMaisContratos(int limite) throws SQLException {
        String sql = "SELECT c.id_cliente, c.nome, COUNT(*) AS total " +
                "FROM contrato ct " +
                "JOIN cliente c ON c.id_cliente = ct.id_cliente " +
                "GROUP BY c.id_cliente, c.nome " +
                "ORDER BY total DESC, c.nome ASC " +
                "LIMIT ?";
        List<ClienteComContagem> lista = new ArrayList<>();
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new ClienteComContagem(
                            rs.getInt("id_cliente"),
                            rs.getString("nome"),
                            rs.getInt("total")));
                }
            }
        }
        return lista;
    }

    // Encerrar (dar baixa) num contrato e liberar o imóvel manualmente (opcional)
    public boolean encerrarContrato(int idContrato) throws SQLException {
        String sqlBusca = "SELECT id_imovel FROM contrato WHERE id_contrato = ?";
        String sqlDelete = "DELETE FROM contrato WHERE id_contrato = ?";
        String sqlUpdateImovel = "UPDATE imovel SET disponivel = 1 WHERE id_imovel = ?";

        try (Connection conn = DB.getConnection()) {
            conn.setAutoCommit(false);
            Integer idImovel = null;

            // Busca o id do imóvel relacionado ao contrato
            try (PreparedStatement ps = conn.prepareStatement(sqlBusca)) {
                ps.setInt(1, idContrato);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idImovel = rs.getInt("id_imovel");
                    } else {
                        System.out.println("Contrato não encontrado: " + idContrato);
                        return false; // Nada para deletar
                    }
                }
            }

            // Libera o imóvel
            if (idImovel != null) {
                try (PreparedStatement ps = conn.prepareStatement(sqlUpdateImovel)) {
                    ps.setInt(1, idImovel);
                    ps.executeUpdate();
                }
            }

            // Remove o contrato
            try (PreparedStatement ps = conn.prepareStatement(sqlDelete)) {
                ps.setInt(1, idContrato);
                ps.executeUpdate();
            }

            conn.commit();
            System.out.println("Contrato encerrado e removido com sucesso!");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    private Contrato map(ResultSet rs) throws SQLException {
        return new Contrato(
                rs.getInt("id_contrato"),
                rs.getInt("id_cliente"),
                rs.getInt("id_imovel"),
                rs.getDouble("valor"),
                rs.getDate("data_inicio").toLocalDate(),
                rs.getDate("data_fim").toLocalDate()
        );
    }

    // DTO simples para relatório "Clientes com mais contratos"
    public static class ClienteComContagem {
        public final int idCliente;
        public final String nome;
        public final int total;

        public ClienteComContagem(int idCliente, String nome, int total) {
            this.idCliente = idCliente;
            this.nome = nome;
            this.total = total;
        }

        @Override
        public String toString() {
            return String.format("Cliente{id=%d, nome='%s', contratos=%d}", idCliente, nome, total);
        }
    }
}
