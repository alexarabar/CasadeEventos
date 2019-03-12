package br.com.alexandrebarboza.casadeeventos.Domain.Relation;

import java.io.Serializable;

/**
 * Created by Alexandre on 10/04/2017.
 */

public class ClienteXTelefone implements Serializable {
    public static final String TABELA    = "ClienteXTelefone";
    public static final String _CLIENTE   = "_cliente";
    public static final String _TELEFONE = "_telefone";

    private long   _cliente;
    private long   _telefone;

    public ClienteXTelefone() {
        this._cliente  = 0;
        this._telefone = 0;
    }

    public long get_cliente() {
        return _cliente;
    }

    public void set_cliente(long _cliente) {
        this._cliente = _cliente;
    }

    public long get_telefone() {
        return _telefone;
    }

    public void set_telefone(long _telefone) {
        this._telefone = _telefone;
    }
}
