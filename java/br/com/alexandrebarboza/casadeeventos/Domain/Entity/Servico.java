package br.com.alexandrebarboza.casadeeventos.Domain.Entity;

import java.io.Serializable;

/**
 * Created by Alexandre on 10/04/2017.
 */

public class Servico implements Serializable {
    public static final String TABELA = "Servicos";
    public static final String _ID = "_id";
    public static final String _TIPO = "_tipo";
    public static final String DESCRICAO = "descricao";
    public static final String VALOR = "valor";

    private long _id;
    private long _tipo;
    private String descricao;
    private double valor;

    public Servico() {
        _id = 0;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long get_tipo() {
        return _tipo;
    }

    public void set_tipo(long _tipo) {
        this._tipo = _tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
            return descricao;
    }
}
