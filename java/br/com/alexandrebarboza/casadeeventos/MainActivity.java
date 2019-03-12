package br.com.alexandrebarboza.casadeeventos;

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
import android.widget.ImageButton;
import android.widget.ListView;

import br.com.alexandrebarboza.casadeeventos.Database.Database;
import br.com.alexandrebarboza.casadeeventos.Domain.Domain;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Evento;
import br.com.alexandrebarboza.casadeeventos.Utility.Connector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Database database;
    private Domain domain;
    private DataFilter  filter;
    private EditText ed_evento_descricao;
    private ImageButton bt_add;
    private ListView ls_eventos;
    private ArrayAdapter<Evento> adapter;
    public static final String p_evento = "EVENTO";

    private class DataFilter implements TextWatcher {
        private ArrayAdapter<Evento> adapter;

        private DataFilter(ArrayAdapter<Evento> adapter) {
            this.adapter = adapter;
        }

        public void setAdapter(ArrayAdapter<Evento> adapter) {
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
        setContentView(R.layout.activity_main);
        bt_add = (ImageButton) findViewById(R.id.button_adicionar_evento);
        bt_add.setOnClickListener(this);
        ed_evento_descricao = (EditText) findViewById(R.id.edit_descricao_evento);
        ls_eventos = (ListView) findViewById(R.id.list_eventos);
        ls_eventos.setOnItemClickListener(this);
        database = Database.getInstance(this);
        domain = domain.getInstance();
    }

    @Override
    protected void onStart() {
        setTitle(getResources().getString(R.string.str_eventos));
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }
        adapter = domain.findEventos(this);
        ls_eventos.setAdapter(adapter);
        filter = new DataFilter(adapter);
        ed_evento_descricao.addTextChangedListener(filter);
        database.Close();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent it = null;
        if(item.getItemId() == R.id.m_clientes) {
            it = new Intent(this, ClienteActivity.class);
        } else if (item.getItemId() == R.id.m_servicos) {
            it = new Intent(this, ServicoActivity.class);
        } else if (item.getItemId() == R.id.m_sair) {
            finish();
        }
        if (it != null) {
            startActivityForResult(it, 0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_adicionar_evento:
                Intent it = new Intent(this, EventoEditActivity.class);
                startActivityForResult(it, 0);
                break;

        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Evento evento = adapter.getItem(position);
        Intent it = new Intent(this, EventoEditActivity.class);
        it.putExtra(p_evento, evento);
        startActivityForResult(it, 0);
    }
    
}
