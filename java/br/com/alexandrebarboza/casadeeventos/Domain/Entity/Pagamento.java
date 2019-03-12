package br.com.alexandrebarboza.casadeeventos.Domain.Entity;

import java.io.Serializable;

/**
 * Created by Alexandre on 10/04/2017.
 */

public class Pagamento implements Serializable {
    public static final String TABELA = "Pagamentos";
    public static final String _ID = "_id";
    public static final String _EVENTO = "_evento";
    public static final String TOTAL = "total";
    public static final String DESCONTO = "desconto";

    private long _id;
    private long _evento;
    private double total;
    private double desconto;

    public Pagamento() {
        _id = 0;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long get_evento() {
        return _evento;
    }

    public void set_evento(long _evento) {
        this._evento = _evento;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }
}
