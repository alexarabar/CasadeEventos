package br.com.alexandrebarboza.casadeeventos;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import br.com.alexandrebarboza.casadeeventos.Database.Database;
import br.com.alexandrebarboza.casadeeventos.Domain.Domain;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Contrato;
import br.com.alexandrebarboza.casadeeventos.Utility.Adapters.ContratoAdapter;
import br.com.alexandrebarboza.casadeeventos.Utility.Adapters.EventoClienteAdapter;
import br.com.alexandrebarboza.casadeeventos.Utility.Connector;

public class EventoContratoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Database database;
    private Domain   domain;
    private EditText ed_contrato;
    private ListView ls_contratos;
    private ArrayAdapter<Contrato> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_contrato);
        ed_contrato  = (EditText) findViewById(R.id.edit_evento_contrato);
        ls_contratos = (ListView) findViewById(R.id.list_evento_contratos);
        ls_contratos.setOnItemClickListener(this);
        database = Database.getInstance(this);
        domain = domain.getInstance();
    }

    @Override
    protected void onStart() {
        setTitle(getResources().getString(R.string.str_contratos));
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }
        long cliente_id = 0;
        String str = getIntent().getStringExtra("CLIENTE");
        if (!str.isEmpty()) {
            cliente_id = domain.getIdCliente(this, str);
        }
        adapter = domain.findContratos(this, cliente_id);
        ls_contratos.setAdapter(adapter);
        database.Close();
        super.onStart();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ContratoAdapter ca  = (ContratoAdapter) adapter;
        Intent it = getIntent();
        it.putExtra("CONTRATO", ca.getItem(position).toString());
        it.putExtra("ID_CONTRATO", ca.getItem(position).get_id());
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
