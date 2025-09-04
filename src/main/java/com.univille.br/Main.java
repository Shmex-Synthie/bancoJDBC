package com.univille.br;

import com.univille.br.dao.ClienteDAO;
import com.univille.br.dao.ContratoDAO;
import com.univille.br.dao.ImovelDAO;
import com.univille.br.model.Cliente;
import com.univille.br.model.Contrato;
import com.univille.br.model.Imovel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner in = new Scanner(System.in);
    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final ImovelDAO imovelDAO = new ImovelDAO();
    private static final ContratoDAO contratoDAO = new ContratoDAO();

    public static void main(String[] args) {
        System.out.println("=== Sistema Imobiliária (JDBC + SQL) ===");
        while (true) {
            menu();
            String op = in.nextLine().trim();
            try {
                switch (op) {
                    case "1": cadastrarCliente(); break;
                    case "2": cadastrarImovel(); break;
                    case "3": cadastrarContrato(); break;
                    case "4": listarImoveisDisponiveis(); break;
                    case "5": listarContratosAtivos(); break;
                    case "6": clientesComMaisContratos(); break;
                    case "7": contratosExpirando30Dias(); break;
                    case "8": encerrarContrato(); break; // opcional
                    case "0": System.out.println("Saindo..."); return;
                    default: System.out.println("Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
                e.printStackTrace();
            }
            System.out.println();
        }
    }

    private static void menu() {
        System.out.println("\nMenu:");
        System.out.println("1) Cadastrar cliente");
        System.out.println("2) Cadastrar imóvel");
        System.out.println("3) Cadastrar contrato de aluguel");
        System.out.println("4) Listar imóveis disponíveis para aluguel");
        System.out.println("5) Listar contratos ativos");
        System.out.println("6) Clientes com mais contratos (top N)");
        System.out.println("7) Contratos expirando nos próximos 30 dias");
        System.out.println("8) Encerrar contrato (liberar imóvel) [opcional]");
        System.out.println("0) Sair");
        System.out.print("Escolha: ");
    }

    private static void cadastrarCliente() throws SQLException {
        System.out.print("Nome: ");
        String nome = in.nextLine();
        System.out.print("CPF: ");
        String cpf = in.nextLine();
        System.out.print("Telefone: ");
        String tel = in.nextLine();
        System.out.print("Email: ");
        String email = in.nextLine();

        Cliente c = new Cliente(nome, cpf, tel, email);
        clienteDAO.inserir(c);
        System.out.println("Cliente cadastrado: " + c);
    }

    private static void cadastrarImovel() throws SQLException {
        System.out.print("Endereço: ");
        String end = in.nextLine();
        System.out.print("Tipo (casa/apto/comercial...): ");
        String tipo = in.nextLine();
        System.out.print("Área (m2) [vazio=pula]: ");
        String areaStr = in.nextLine();
        Double area = areaStr.isBlank() ? null : Double.parseDouble(areaStr);

        System.out.print("Quartos [vazio=pula]: ");
        String quartosStr = in.nextLine();
        Integer quartos = quartosStr.isBlank() ? null : Integer.parseInt(quartosStr);

        System.out.print("Banheiros [vazio=pula]: ");
        String banheirosStr = in.nextLine();
        Integer banheiros = banheirosStr.isBlank() ? null : Integer.parseInt(banheirosStr);

        System.out.print("Valor de aluguel sugerido [vazio=pula]: ");
        String valorStr = in.nextLine();
        Double valor = valorStr.isBlank() ? null : Double.parseDouble(valorStr);

        Imovel i = new Imovel(end, tipo, area, quartos, banheiros, valor);
        imovelDAO.inserir(i);
        System.out.println("Imóvel cadastrado: " + i);
    }

    private static void cadastrarContrato() throws SQLException {
        System.out.print("ID do Cliente: ");
        int idCliente = Integer.parseInt(in.nextLine());
        System.out.print("ID do Imóvel: ");
        int idImovel = Integer.parseInt(in.nextLine());
        System.out.print("Valor do aluguel: ");
        double valor = Double.parseDouble(in.nextLine());
        System.out.print("Data início (YYYY-MM-DD): ");
        LocalDate ini = LocalDate.parse(in.nextLine());
        System.out.print("Data fim (YYYY-MM-DD): ");
        LocalDate fim = LocalDate.parse(in.nextLine());

        Contrato ct = new Contrato(idCliente, idImovel, valor, ini, fim);
        contratoDAO.inserir(ct);
        System.out.println("Contrato cadastrado: " + ct);
    }

    private static void listarImoveisDisponiveis() throws SQLException {
        List<Imovel> lista = imovelDAO.listarDisponiveis();
        if (lista.isEmpty()) {
            System.out.println("Nenhum imóvel disponível.");
        } else {
            System.out.println("Imóveis disponíveis:");
            lista.forEach(System.out::println);
        }
    }

    private static void listarContratosAtivos() throws SQLException {
        var lista = contratoDAO.listarAtivos();
        if (lista.isEmpty()) {
            System.out.println("Nenhum contrato ativo.");
        } else {
            System.out.println("Contratos ativos:");
            lista.forEach(System.out::println);
        }
    }

    private static void clientesComMaisContratos() throws SQLException {
        System.out.print("Mostrar top N (ex.: 5): ");
        int n = Integer.parseInt(in.nextLine());
        var lista = contratoDAO.clientesComMaisContratos(n);
        if (lista.isEmpty()) {
            System.out.println("Sem dados.");
        } else {
            System.out.println("Clientes com mais contratos:");
            lista.forEach(System.out::println);
        }
    }

    private static void contratosExpirando30Dias() throws SQLException {
        var lista = contratoDAO.listarExpirandoEm30Dias();
        if (lista.isEmpty()) {
            System.out.println("Nenhum contrato expirando nos próximos 30 dias.");
        } else {
            System.out.println("Contratos expirando nos próximos 30 dias:");
            lista.forEach(System.out::println);
        }
    }

    private static void encerrarContrato() throws SQLException {
        System.out.print("ID do contrato a encerrar: ");
        int id = Integer.parseInt(in.nextLine());
        contratoDAO.encerrarContrato(id);
        System.out.println("Contrato encerrado e imóvel liberado (disponível = TRUE).");
    }
}
