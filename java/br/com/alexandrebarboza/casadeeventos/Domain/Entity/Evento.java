package br.com.alexandrebarboza.casadeeventos.Domain.Entity;

import java.io.Serializable;

/**
 * Created by Alexandre on 10/04/2017.
 */

public class Evento implements Serializable {
    public static final String TABELA = "Eventos";
    public static final String _ID = "_id";
    public static final String _CONTRATO = "_contrato";
    public static final String DESCRICAO = "descricao";

    private long   _id;
    private long   _contrato;
    private String descricao;

    public Evento() {
        _id = 0;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long get_contrato() {
        return _contrato;
    }

    public void set_contrato(long _contrato) {
        this._contrato = _contrato;
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
