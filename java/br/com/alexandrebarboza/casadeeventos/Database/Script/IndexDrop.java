package br.com.alexandrebarboza.casadeeventos.Database.Script;

/**
 * Created by Alexandre on 10/04/2017.
 */

public class IndexDrop {
    private static String Drop(String index) {
        return "DROP INDEX IF EXISTS " + index + ";";
    }
    public static String uk_cliente_x_telefone(){
        return Drop("uk_cliente_x_telefone");
    }
    public static String uk_cliente_x_email(){
        return Drop("uk_cliente_x_email");
    }
    public static String uk_cliente_x_endereco(){
        return Drop("uk_cliente_x_endereco");
    }
    public static String uk_endereco(){
        return Drop("uk_endereco");
    }
}
