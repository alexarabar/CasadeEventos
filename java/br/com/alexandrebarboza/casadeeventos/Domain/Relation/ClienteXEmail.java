package br.com.alexandrebarboza.casadeeventos.Domain.Relation;

import java.io.Serializable;

/**
 * Created by Alexandre on 10/04/2017.
 */

public class ClienteXEmail implements Serializable {
    public static final String TABELA    = "ClienteXEmail";
    public static final String _CLIENTE   = "_cliente";
    public static final String _EMAIL = "_email";

    private long   _cliente;
    private long   _email;

    public ClienteXEmail() {
        this._cliente  = 0;
        this._email = 0;
    }

    public long get_cliente() {
        return _cliente;
    }

    public void set_cliente(long _cliente) {
        this._cliente = _cliente;
    }

    public long get_email() {
        return _email;
    }

    public void set_email(long _email) {
        this._email = _email;
    }
}
