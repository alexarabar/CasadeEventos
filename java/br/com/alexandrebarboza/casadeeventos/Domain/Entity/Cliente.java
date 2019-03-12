package br.com.alexandrebarboza.casadeeventos.Domain.Entity;

import java.io.Serializable;

/**
 * Created by Alexandre on 10/04/2017.
 */

public class Cliente implements Serializable {
    public static final String TABELA = "Clientes";
    public static final String _ID = "_id";
    public static final String CNPJ = "cnpj";
    public static final String RAZAO_SOCIAL = "razao_social";

    private long   _id;
    private String cnpj;
    private String razao_social;

    public Cliente() {
        _id = 0;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazao_social() {
        return razao_social;
    }

    public void setRazao_social(String razao_social) {
        this.razao_social = razao_social;
    }

    @Override
    public String toString() {
        return razao_social;
    }
}
