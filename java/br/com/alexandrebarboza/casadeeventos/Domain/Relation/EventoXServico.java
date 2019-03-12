package br.com.alexandrebarboza.casadeeventos.Domain.Relation;

import java.io.Serializable;

/**
 * Created by Alexandre on 11/04/2017.
 */

public class EventoXServico implements Serializable {
    public static final String TABELA   = "EventoXServico";
    public static final String _EVENTO  = "_evento";
    public static final String _SERVICO = "_servico";

    private long   _evento;
    private long   _servico;

    public EventoXServico() {
        _evento = 0;
        _servico = 0;
    }

    public long get_evento() {
        return _evento;
    }

    public void set_evento(long _evento) {
        this._evento = _evento;
    }

    public long get_servico() {
        return _servico;
    }

    public void set_servico(long _servico) {
        this._servico = _servico;
    }
}
