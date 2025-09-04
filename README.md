# bancoJDBC

## Equipe

|        Equipe       |              Email            |
|---------------------|-------------------------------|
| Gabriel Mesadri     | gwmesadri@gmail.com           |
| Gustavo Frazzon     | gustavofrazzon22@gmail.com    |
| Gustavo Schumacher  | gustavoschumacher7@gmail.com  |

## Configurações

| Item           |    Valor    |
|----------------|-------------|
| Banco de Dados | MySQL       |
| Schema         | imobiliaria |

## Diagrama de classe da UML

![Diagrama de classe](/Diagrama-de-Classe.jpg)

## Diagrama MER

![MER](/Diagrama-MER.jpg)

## Instruções SQL

Criação do schema e tabelas.
```SQL
-- Criação do schema
CREATE SCHEMA IF NOT EXISTS meu_banco_de_dados;
USE meu_banco_de_dados;

-- tabela CLIENTE
CREATE TABLE cliente (
    id_cliente INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    email VARCHAR(100)
);

-- tabela IMÓVEL
CREATE TABLE imovel (
    id_imovel INT PRIMARY KEY AUTO_INCREMENT,
    endereco VARCHAR(200) NOT NULL,
    tipo VARCHAR(50), -- exemplo: apartamento, casa, sala comercial
    area FLOAT,
    quartos INT,
    banheiros INT,
    valor_aluguel DECIMAL(10,2),
    disponivel BOOLEAN DEFAULT TRUE
);

-- tabela CONTRATO
CREATE TABLE contrato (
    id_contrato INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    id_imovel INT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
    FOREIGN KEY (id_imovel) REFERENCES imovel(id_imovel)
);

```

Inserção de registros.
```SQL
-- para inserir clientes
INSERT INTO cliente (nome, cpf, telefone, email) VALUES
('Nome', '111.111.111-11', 'telefone1', 'email1'),
('Nome II', '222.222.222-22', 'telefone2', 'email2'),
('Nome o Terceiro', '333.333.333-33', 'telefone3', 'email3');

-- para inserir imóveis
INSERT INTO imovel (endereco, tipo, area, quartos, banheiros, valor_aluguel, disponivel) VALUES
('Rua das Flores, 123, São Paulo, SP', 'Apartamento', 75.5, 2, 2, 2500.00, TRUE),
('Av. Brasil, 456, Rio de Janeiro, RJ', 'Casa', 120.0, 3, 3, 3500.00, TRUE),
('Rua Central, 789, Belo Horizonte, MG', 'Sala Comercial', 50.0, 0, 1, 2000.00, TRUE);

-- para criar novos contratos
INSERT INTO contrato (id_cliente, id_imovel, valor, data_inicio, data_fim) VALUES
(1, 1, 2500.00, '2025-09-01', '2026-08-31'),
(2, 2, 3500.00, '2025-09-15', '2026-09-14'),
(3, 3, 2000.00, '2025-10-01', '2026-09-30');
```
