package br.com.alexandrebarboza.casadeeventos.Domain.Relation;

import java.io.Serializable;

/**
 * Created by Alexandre on 10/04/2017.
 */

public class ClienteXEndereco implements Serializable {
    public static final String TABELA    = "ClienteXEndereco";
    public static final String _CLIENTE   = "_cliente";
    public static final String _ENDERECO = "_endereco";

    private long   _cliente;
    private long   _endereco;

    public ClienteXEndereco() {
        this._cliente  = 0;
        this._endereco = 0;
    }

    public long get_cliente() {
        return _cliente;
    }

    public void set_cliente(long _cliente) {
        this._cliente = _cliente;
    }

    public long get_endereco() {
        return _endereco;
    }

    public void set_endereco(long _endereco) {
        this._endereco = _endereco;
    }
}
