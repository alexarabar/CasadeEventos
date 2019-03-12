package br.com.alexandrebarboza.casadeeventos.Database.Script;

/**
 * Created by Alexandre on 10/04/2017.
 */

public class IndexCreate {
    public static String uk_endereco(){
        return "CREATE UNIQUE INDEX IF NOT EXISTS uk_endereco ON Enderecos (logradouro COLLATE NOCASE ASC, numero COLLATE NOCASE ASC, complemento COLLATE NOCASE ASC, bairro COLLATE NOCASE ASC, cidade COLLATE NOCASE ASC, uf COLLATE NOCASE ASC, cep COLLATE NOCASE ASC);";
    }
    public static String uk_cliente_x_telefone(){
        return "CREATE UNIQUE INDEX IF NOT EXISTS uk_cliente_x_telefone ON ClienteXTelefone (_cliente COLLATE NOCASE ASC, _telefone COLLATE NOCASE ASC);";
    }
    public static String uk_cliente_x_email(){
        return "CREATE UNIQUE INDEX IF NOT EXISTS uk_cliente_x_email ON ClienteXEmail (_cliente COLLATE NOCASE ASC, _email COLLATE NOCASE ASC);";
    }
    public static String uk_cliente_x_endereco(){
        return "CREATE UNIQUE INDEX IF NOT EXISTS uk_cliente_x_endereco ON ClienteXEndereco (_cliente COLLATE NOCASE ASC, _endereco COLLATE NOCASE ASC);";
    }
}
