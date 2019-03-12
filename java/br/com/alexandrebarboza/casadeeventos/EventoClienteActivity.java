package br.com.alexandrebarboza.casadeeventos;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import br.com.alexandrebarboza.casadeeventos.Database.Database;
import br.com.alexandrebarboza.casadeeventos.Domain.Domain;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Cliente;
import br.com.alexandrebarboza.casadeeventos.Utility.Adapters.EventoClienteAdapter;
import br.com.alexandrebarboza.casadeeventos.Utility.Connector;

public class EventoClienteActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Database database;
    private Domain   domain;
    private EventoClienteActivity.DataFilter filter;
    private EditText ed_cliente;
    private ListView ls_clientes;
    private ArrayAdapter<Cliente> adapter;

    private class DataFilter implements TextWatcher {
        private ArrayAdapter <Cliente> adapter;

        private DataFilter(ArrayAdapter <Cliente> adapter) {
            this.adapter = adapter;
        }

        public void setAdapter(ArrayAdapter <Cliente> adapter) {
            this.adapter = adapter;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            adapter.getFilter().filter(s);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_cliente);
        ed_cliente = (EditText) findViewById(R.id.edit_evento_cliente2);
        ls_clientes = (ListView) findViewById(R.id.list_evento_clientes);
        ls_clientes.setOnItemClickListener(this);
        database = Database.getInstance(this);
        domain = domain.getInstance();
    }

    @Override
    protected void onStart() {
        setTitle(getResources().getString(R.string.str_clientes));
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }
        adapter = domain.findClientes(this, false);
        ls_clientes.setAdapter(adapter);
        filter = new EventoClienteActivity.DataFilter(adapter);
        ed_cliente.addTextChangedListener(filter);
        database.Close();
        super.onStart();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EventoClienteAdapter eca  = (EventoClienteAdapter) adapter;
        Intent it = getIntent();
        it.putExtra("CLIENTE", eca.getItem(position).getRazao_social());
        setResult(Activity.RESULT_OK, it);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        menu.getItem(0).setVisible(false);
        menu.getItem(2).setVisible(false);
        menu.getItem(3).setVisible(false);
        menu.getItem(4).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_voltar:
                setResult(Activity.RESULT_CANCELED, null);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
