package br.com.alexandrebarboza.casadeeventos.Database.Script;

/**
 * Created by Alexandre on 10/04/2017.
 */

public class TableDrop {
    private static String Drop(String table) {
        return "DROP TABLE IF EXISTS " + table + ";";
    }
    public static String ClienteXTelefone(){
        return Drop("ClienteXTelefone");
    }
    public static String ClienteXEmail(){
        return Drop("ClienteXEmail");
    }
    public static String ClienteXEndereco(){
        return Drop("ClienteXEndereco");
    }
    public static String EventoXServico(){
        return Drop("EventoXServico");
    }
    public static String Pagamentos(){
        return Drop("Pagamentos");
    }
    public static String Eventos(){
        return Drop("Eventos");
    }
    public static String Tipos(){
        return Drop("Tipos");
    }
    public static String Servicos(){
        return Drop("Servicos");
    }
    public static String Contratos(){
        return Drop("Contratos");
    }
    public static String Clientes(){
        return Drop("Clientes");
    }
    public static String Telefones(){
        return Drop("Telefones");
    }
    public static String Emails(){
        return Drop("Emails");
    }
    public static String Enderecos(){
        return Drop("Enderecos");
    }
}
