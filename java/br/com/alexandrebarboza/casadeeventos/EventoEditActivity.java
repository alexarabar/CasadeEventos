package br.com.alexandrebarboza.casadeeventos;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;

import br.com.alexandrebarboza.casadeeventos.Database.Database;
import br.com.alexandrebarboza.casadeeventos.Domain.Domain;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Contrato;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Evento;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Pagamento;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Servico;
import br.com.alexandrebarboza.casadeeventos.Domain.Relation.EventoXServico;
import br.com.alexandrebarboza.casadeeventos.Utility.Adapters.Adapters;
import br.com.alexandrebarboza.casadeeventos.Utility.Connector;
import br.com.alexandrebarboza.casadeeventos.Utility.Messages.InputPagamento;
import br.com.alexandrebarboza.casadeeventos.Utility.Messages.Output;
import br.com.alexandrebarboza.casadeeventos.Utility.Utility;

public class EventoEditActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnDismissListener, DialogInterface.OnCancelListener{
    private final int FIND_CLIENTE  = 1;
    private final int FIND_CONTRATO = 2;
    private final int FIND_SERVICOS = 3;
    private final int UPDATE_PAGAMENTO = 4;
    private final int DELETE_PAGAMENTO = 5;
    private final int DELETE_EVENTO = 6;
    private int request;
    private long        id_contrato;
    private EditText    ed_descricao;
    private EditText    ed_cliente;
    private EditText    ed_contrato;
    private ImageButton ib_cliente;
    private ImageButton ib_contrato;
    private ImageButton ib_servicos;
    private Spinner     sp_servicos;
    private EditText    ed_pagamento;
    private ImageButton ib_pagamento_update;
    private ImageButton ib_pagamento_delete;

    private ArrayAdapter<String> ad_servicos;
    private ArrayAdapter <Servico>  servicos;
    private ArrayAdapter <EventoXServico> evento_x_servico;

    private Database  database;
    private Domain    domain;
    private Evento    evento;
    private Pagamento pagamento;

    private ArrayList<String> getArrayList() {
       ArrayList<String> res = new ArrayList<String>();
       for (int i = 0; i < ad_servicos.getCount(); i++) {
           res.add(ad_servicos.getItem(i).toString());
       }
       return res;
    }
    private double getTotal() {
        double result = 0.00;
        if (ad_servicos.getCount() == 0 || !Connector.OpenDatabase(getResources(), this, database, domain, true)) {
            return result;
        }
        result = domain.getTotalServicos(ad_servicos);
        database.Close();
        return result;
    }
    private void Excluir() {
        request = DELETE_EVENTO;
        String st1, st2, st3;
        st1 = getResources().getString(R.string.str_delete);
        st2 = getResources().getString(R.string.str_eventos);
        st2 = st2.substring(0, st2.length() -1);
        st2 = st2.toLowerCase();
        st3 = ed_descricao.getText().toString();
        Output.Question(this, st1 + " o " + st2 + " " + st3, getResources().getString(R.string.str_remove));
    }

    private void Salvar() {
        if (!Connector.OpenDatabase(getResources(), this, database, domain, true)) {
            return;
        }
        boolean result  = false;
        int[] control   = {0};
        if (evento.get_id() == 0) {   // Modo de Inclusão.
            result = Connector.SaveAddEvento(this, domain, evento, ed_descricao.getText().toString(), id_contrato, ad_servicos);
            control[0] = 1;
        } else {                      // Modo de Alteração.
            result = Connector.SaveUpdateEvento(this, domain, evento, servicos, pagamento, ed_descricao.getText().toString(), Utility.getDoubleOfCurrencyForBR(ed_pagamento.getText().toString()), id_contrato, ad_servicos, sp_servicos, control);
        }
        if (result) {
            if (control[0] == 1) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_success), Toast.LENGTH_SHORT).show();
            }
            finish();
        }
        database.Close();
    }
    private void Preencher() {
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }
        ed_descricao.setText(evento.getDescricao());
        id_contrato = evento.get_contrato();
        Contrato contrato = domain.selectContrato(id_contrato);
        ed_contrato.setText(contrato.toString());
        ed_cliente.setText(domain.getTextCliente(this, contrato.get_cliente()));
        if (domain.setEventoServicos(servicos, evento_x_servico, ad_servicos, evento.get_id())) {
            Utility.servicoSort(servicos);
            Adapters.adapterSort(ad_servicos);
        }
        pagamento = new Pagamento();
        if (domain.setEventoPagamento(evento.get_id(), pagamento)) {
            ed_pagamento.setText("$" + Utility.convertCurrencyForBR(Utility.getDoubleFormatForUS(pagamento.getTotal(), true)));
            Utility.resetImages(true, null, ib_pagamento_delete);
        }
        database.Close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_edit);
        request = 0;
        ed_descricao = (EditText) findViewById(R.id.edit_evento_descricao);
        ib_cliente = (ImageButton) findViewById(R.id.button_procurar_evento_cliente);
        ib_cliente.setOnClickListener(this);
        ed_cliente = (EditText) findViewById(R.id.edit_evento_cliente1);
        ed_cliente.setKeyListener(null);
        ed_cliente.setOnClickListener(this);
        ib_contrato = (ImageButton) findViewById(R.id.button_procurar_evento_contrato);
        ib_contrato.setOnClickListener(this);
        ed_contrato = (EditText) findViewById(R.id.edit_evento_contrato);
        ed_contrato.setKeyListener(null);
        ib_servicos = (ImageButton) findViewById(R.id.button_procurar_evento_servicos);
        ib_servicos.setOnClickListener(this);
        sp_servicos = (Spinner) findViewById(R.id.spinner_evento_servicos);
        ad_servicos = Adapters.GetSpinner(this, sp_servicos);
        ad_servicos.setNotifyOnChange(true);
        ed_pagamento = (EditText) findViewById(R.id.edit_evento_pagamento);
        ed_pagamento.setKeyListener(null);
        ib_pagamento_update = (ImageButton) findViewById(R.id.button_alterar_evento_pagamento);
        ib_pagamento_delete = (ImageButton) findViewById(R.id.button_excluir_evento_pagamento);
        ib_pagamento_update.setOnClickListener(this);
        ib_pagamento_delete.setOnClickListener(this);

        servicos         = new ArrayAdapter <Servico> (this, 0);
        evento_x_servico = new ArrayAdapter <EventoXServico> (this, 0);

        database = Database.getInstance(this);
        domain = domain.getInstance();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(MainActivity.p_evento)) { // modo de alteração;
            evento = (Evento) bundle.getSerializable(MainActivity.p_evento);
            View txview = findViewById(R.id.label_evento_pagamento);
            View layout   = findViewById(R.id.layout_evento_pagamento);
            txview.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);
            Preencher();
        } else {
            evento = new Evento(); // modo de inclusão.
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle(getResources().getString(R.string.str_evento_editar));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        if (evento.get_id() > 0)
            menu.getItem(1).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_salvar:
                int flag = 0;
                if (ed_descricao.getText().toString().isEmpty()) {
                    flag = -2;
                } else if (ed_contrato.getText().toString().isEmpty()) {
                    flag = -3;
                } else if (sp_servicos.getCount() == 0) {
                    flag = -4;
                } else {
                    if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
                        return false;
                    }
                    if (evento.get_id() == 0) { // Inclusão.
                        flag = Connector.LoadAddEvento(domain, ed_descricao.getText().toString(), id_contrato);
                    } else {                    // Alteração.
                        flag = Connector.LoadUpdateEvento(domain, evento, ed_descricao.getText().toString(), id_contrato);
                    }
                    database.Close();
                }
                String msg;
                switch (flag) {
                    case -4:  // Nenhum serviço no evento.
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_servico_empty), Toast.LENGTH_SHORT).show();
                        break;
                    case -3:  // Contrato não preenchido.
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_contrato_empty), Toast.LENGTH_SHORT).show();
                        break;
                    case -2:  // Descrição não preenchida.
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_descricao_empty), Toast.LENGTH_SHORT).show();
                        break;
                    case -1:  // Erro na consulta SQL.
                        msg = getResources().getString(R.string.str_err_sql_query) + " " + getResources().getString(R.string.str_msg_plus) + "\n" + domain.getError();
                        Output.Alert(this, getResources().getString(R.string.str_fail), msg);
                        break;
                    case 0:
                        break;
                    case 1:
                        Salvar();
                        break;
                    case 2: // Descrição já existe.
                        String s = getResources().getString(R.string.str_descricao);
                        s = s.substring(0, s.length() -1);
                        msg = s + " " + ed_descricao.getText().toString()  + " " + getResources().getString(R.string.str_found) + "!";
                        Output.Alert(this, getResources().getString(R.string.str_info_repeat), msg);
                        break;
                    case 3:  // Já existe um evento cadastrado para o contrato.
                        msg = getResources().getString(R.string.str_contrato_found) + " " + ed_contrato.getText().toString() + ".";
                        Output.Alert(this, getResources().getString(R.string.str_info_repeat), msg);
                        break;
                }
                break;
            case R.id.m_excluir:
                Excluir();
                break;
            case R.id.m_limpar:
                ad_servicos.clear();
                if (evento.get_id() == 0) {
                    ed_cliente.getText().clear();
                    ed_contrato.getText().clear();
                } else {
                    Preencher();
                }
                break;
            case R.id.m_cancelar:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        Intent it = null;
        Bundle bundle = new Bundle();
        InputPagamento input_pagamento;
        switch (v.getId()) {
            case R.id.edit_evento_cliente1:
                if (!ed_cliente.getText().toString().isEmpty()) {
                    ed_cliente.getText().clear();
                    ed_contrato.getText().clear();
                    id_contrato = 0;
                }
                break;
            case R.id.button_procurar_evento_cliente:
                request = FIND_CLIENTE;
                it = new Intent(this, EventoClienteActivity.class);
                break;
            case R.id.button_procurar_evento_contrato:
                request = FIND_CONTRATO;
                it = new Intent(this, EventoContratoActivity.class);
                bundle.putString("CLIENTE", ed_cliente.getText().toString());
                it.putExtras(bundle);
                break;
            case R.id.button_procurar_evento_servicos:
                request = FIND_SERVICOS;
                it = new Intent(this, EventoServicosActivity.class);
                bundle.putStringArrayList("LIST_SERVICOS", getArrayList());
                it.putExtras(bundle);
                break;
            case R.id.button_alterar_evento_pagamento:
                request = UPDATE_PAGAMENTO;
                input_pagamento = new InputPagamento(this, getResources().getString(R.string.str_pagamento));
                input_pagamento.setInputPagamento(getTotal(), pagamento);
                break;
            case R.id.button_excluir_evento_pagamento:
                request = DELETE_PAGAMENTO;
                String st1 = getResources().getString(R.string.str_delete);
                String st2 = getResources().getString(R.string.str_pagamento);
                st2 = st2.substring(0, st2.length() - 1).toLowerCase();
                String st3 = ed_pagamento.getText().toString();
                Output.Question(this, st1 + " o " + st2 + " de " + st3 + "?", getResources().getString(R.string.str_remove));
                break;
        }
        if (it != null) {
            startActivityForResult(it, request, bundle);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                switch (requestCode) {
                    case FIND_CLIENTE:
                        if (!ed_cliente.getText().toString().equals(data.getStringExtra("CLIENTE"))) {
                            ed_cliente.setText(data.getStringExtra("CLIENTE"));
                            ed_contrato.getText().clear();
                            id_contrato = 0;
                        }
                        break;
                    case FIND_CONTRATO:
                        id_contrato = data.getLongExtra("ID_CONTRATO", 0);
                        ed_contrato.setText(data.getStringExtra("CONTRATO"));
                        break;
                    case FIND_SERVICOS:
                        ad_servicos.clear();
                        ArrayList<String> als = data.getStringArrayListExtra("LIST_SERVICOS");
                        for (int i = 0; i < als.size(); i++) {
                             ad_servicos.add(als.get(i));
                        }
                        Adapters.adapterSort(ad_servicos);
                        ad_servicos.notifyDataSetChanged();
                        break;
                }
            }
        }
        request = 0;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Intent it = this.getIntent();
        switch (request) {
            case UPDATE_PAGAMENTO:
                if (it.hasExtra("PAGAMENTO_TOTAL")) {
                    ed_pagamento.setText("$" + Utility.convertCurrencyForBR(Utility.getDoubleFormatForUS(it.getDoubleExtra("PAGAMENTO_TOTAL", 0.00), true)));
                }
                Utility.resetImages(true, null, ib_pagamento_delete);
                break;
            case DELETE_PAGAMENTO:
                if (it.hasExtra("PAGAMENTO_TOTAL")) {
                    it.removeExtra("PAGAMENTO_TOTAL");
                }
                ed_pagamento.setText("");
                Utility.resetImages(false, null, ib_pagamento_delete);
                break;
            case DELETE_EVENTO:
                if (!Connector.OpenDatabase(getResources(), this, database, domain, true)) {
                    return;
                }
                String[] v = {""};
                boolean flag = Connector.DeleteEvento(this, domain, evento.get_id(), v);
                String str = v[0];
                if (!str.isEmpty()) {
                    String msg = getResources().getString(R.string.str_err_delete) + " " + str.toLowerCase() + getResources().getString(R.string.str_msg_plus) + "\n" + domain.getError();
                    Output.Alert(this, getResources().getString(R.string.str_fail), msg);
                } else {
                    if (flag) {
                        Toast.makeText(this, getResources().getString(R.string.str_success), Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
                break;
        }
        request = 0;
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }
}
