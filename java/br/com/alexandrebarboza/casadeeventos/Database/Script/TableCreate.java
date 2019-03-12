package br.com.alexandrebarboza.casadeeventos.Database.Script;

/**
 * Created by Alexandre on 10/04/2017.
 */

public class TableCreate {
    public static String Telefones(){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS Telefones ( ");
        sb.append("_id INTEGER CONSTRAINT telefone_id PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("numero VARCHAR (15) NOT NULL ON CONFLICT FAIL CONSTRAINT uk_telefone_numero UNIQUE COLLATE NOCASE);");
        return sb.toString();
    }
    public static String Emails(){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS Emails ( ");
        sb.append("_id INTEGER CONSTRAINT email_id PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("endereco VARCHAR (65) CONSTRAINT uk_email_endereco UNIQUE ON CONFLICT FAIL NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String Enderecos(){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS Enderecos ( ");
        sb.append("_id INTEGER CONSTRAINT endereco_id PRIMARY KEY ON CONFLICT FAIL AUTOINCREMENT NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("logradouro VARCHAR (50) NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("numero INTEGER NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("complemento VARCHAR (15) NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("bairro VARCHAR (30) NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("cidade VARCHAR (40) NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("uf VARCHAR (2) NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("cep INTEGER (8) NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String Clientes(){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS Clientes ( ");
        sb.append("_id INTEGER CONSTRAINT pk_cliente PRIMARY KEY ASC ON CONFLICT FAIL AUTOINCREMENT NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("cnpj VARCHAR (15) CONSTRAINT uk_cnpj UNIQUE ON CONFLICT FAIL NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("razao_social VARCHAR (40) CONSTRAINT uk_razao_social UNIQUE ON CONFLICT FAIL NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String Contratos(){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS Contratos ( ");
        sb.append("_id INTEGER CONSTRAINT pk_contrato PRIMARY KEY ASC ON CONFLICT FAIL AUTOINCREMENT NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("_cliente INTEGER CONSTRAINT fk_contrato_cliente REFERENCES Clientes (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("data_inicial DATETIME NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("data_final DATETIME NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String Tipos() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS Tipos ( ");
        sb.append("_id INTEGER NOT NULL ON CONFLICT FAIL COLLATE NOCASE CONSTRAINT pk_tipo PRIMARY KEY ASC ON CONFLICT FAIL AUTOINCREMENT, ");
        sb.append("descricao VARCHAR (25) CONSTRAINT uk_tipo_descricao UNIQUE ON CONFLICT FAIL NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String Servicos(){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS Servicos ( ");
        sb.append("_id INTEGER CONSTRAINT pk_servico PRIMARY KEY ASC ON CONFLICT FAIL AUTOINCREMENT NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("_tipo INTEGER NOT NULL ON CONFLICT FAIL COLLATE NOCASE CONSTRAINT fk_servico_tipo REFERENCES Tipos (_id) ON DELETE CASCADE ON UPDATE CASCADE, ");
        sb.append("descricao VARCHAR (40) NOT NULL ON CONFLICT FAIL COLLATE NOCASE CONSTRAINT uk_servico_descricao UNIQUE ON CONFLICT FAIL, ");
        sb.append("valor DECIMAL (9, 2) NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String Eventos(){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS Eventos ( ");
        sb.append("_id INTEGER CONSTRAINT pk_evento PRIMARY KEY ASC ON CONFLICT FAIL AUTOINCREMENT NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("_contrato INTEGER CONSTRAINT fk_contrato REFERENCES Contratos (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE CONSTRAINT uk_contrato UNIQUE ON CONFLICT FAIL, ");
        sb.append("descricao VARCHAR (40) NOT NULL ON CONFLICT FAIL COLLATE NOCASE CONSTRAINT uk_evento UNIQUE ON CONFLICT FAIL);");
        return sb.toString();
    }
    public static String Pagamentos(){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS Pagamentos ( ");
        sb.append("_id INTEGER CONSTRAINT pk_pagamento PRIMARY KEY ASC ON CONFLICT FAIL AUTOINCREMENT NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("_evento INTEGER CONSTRAINT fk_evento REFERENCES Eventos (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("total DECIMAL (11, 2) NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("desconto DECIMAL (11, 2) NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String ClienteXTelefone(){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ClienteXTelefone ( ");
        sb.append("_cliente INTEGER NOT NULL ON CONFLICT FAIL COLLATE NOCASE CONSTRAINT fk_cliente_x1 REFERENCES Clientes (_id) ON DELETE CASCADE ON UPDATE CASCADE, ");
        sb.append("_telefone INTEGER NOT NULL ON CONFLICT FAIL COLLATE NOCASE CONSTRAINT fk_telefone_x REFERENCES Telefones (_id) ON DELETE CASCADE ON UPDATE CASCADE);");
        return sb.toString();
    }
    public static String ClienteXEmail(){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ClienteXEmail ( ");
        sb.append("_cliente INTEGER CONSTRAINT fk_cliente_x2 REFERENCES Clientes (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("_email INTEGER CONSTRAINT fk_email_x REFERENCES Emails (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String ClienteXEndereco(){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ClienteXEndereco ( ");
        sb.append("_cliente INTEGER CONSTRAINT fk_cliente_x3 REFERENCES Clientes (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE, ");
        sb.append("_endereco INTEGER CONSTRAINT fk_endereco_x REFERENCES Enderecos (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
    public static String EventoXServico() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS EventoXServico ( ");
        sb.append("_evento INTEGER NOT NULL ON CONFLICT FAIL COLLATE NOCASE CONSTRAINT fk_evento_x REFERENCES Eventos (_id) ON DELETE CASCADE ON UPDATE CASCADE, ");
        sb.append("_servico INTEGER CONSTRAINT fk_servico_x REFERENCES Servicos (_id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL ON CONFLICT FAIL COLLATE NOCASE);");
        return sb.toString();
    }
}
