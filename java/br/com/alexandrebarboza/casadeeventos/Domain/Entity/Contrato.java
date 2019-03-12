package br.com.alexandrebarboza.casadeeventos.Domain.Entity;

import java.io.Serializable;
import java.sql.Date;

import br.com.alexandrebarboza.casadeeventos.Utility.Dates;

/**
 * Created by Alexandre on 10/04/2017.
 */

public class Contrato implements Serializable {
    public static final String TABELA = "Contratos";
    public static final String _ID = "_id";
    public static final String _CLIENTE = "_cliente";
    public static final String DATA_INICIAL = "data_inicial";
    public static final String DATA_FINAL = "data_final";

    private long _id;
    private long _cliente;
    private Date data_inicial;
    private Date data_final;

    public Contrato() {
        _id = 0;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long get_cliente() {
        return _cliente;
    }

    public void set_cliente(long _cliente) {
        this._cliente = _cliente;
    }

    public Date getData_inicial() {
        return data_inicial;
    }

    public void setData_inicial(Date data_inicial) {
        this.data_inicial = data_inicial;
    }

    public Date getData_final() {
        return data_final;
    }

    public void setData_final(Date data_final) {
        this.data_final = data_final;
    }

    @Override
    public String toString() {
        if (data_inicial != null && data_final != null) {
            String s1 = Dates.getShortFormatFromDateTime(data_inicial);
            String s2 = Dates.getShortFormatFromDateTime(data_final);
            return "de: " + s1 + " a: " + s2;
        }
        return null;
    }
}
