package br.com.alexandrebarboza.casadeeventos.Domain.Entity;

import java.io.Serializable;

/**
 * Created by Alexandre on 11/04/2017.
 */

public class Tipo implements Serializable {
    public static final String TABELA = "Tipos";
    public static final String _ID = "_id";
    public static final String DESCRICAO = "descricao";

    private long _id;
    private String descricao;

    public Tipo() {
        _id = 0;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
