package br.com.alexandrebarboza.casadeeventos;

import android.app.Activity;
import android.content.DialogInterface;
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
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Cliente;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Servico;
import br.com.alexandrebarboza.casadeeventos.Utility.Connector;

public class ServicoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Database database;
    private Domain domain;
    private ServicoActivity.DataFilter filter;
    private EditText ed_descricao_servico;
    private ImageButton bt_add;
    private ListView ls_servicos;
    private ArrayAdapter<Servico> adapter;
    public static final String p_servico = "SERVICO";

    private class DataFilter implements TextWatcher {
        private ArrayAdapter<Servico> adapter;

        private DataFilter(ArrayAdapter<Servico> adapter) {
            this.adapter = adapter;
        }

        public void setAdapter(ArrayAdapter<Servico> adapter) {
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
        setContentView(R.layout.activity_servico);
        bt_add = (ImageButton) findViewById(R.id.button_adicionar_servico);
        bt_add.setOnClickListener(this);
        ed_descricao_servico = (EditText) findViewById(R.id.edit_descricao_servico);
        ls_servicos = (ListView) findViewById(R.id.list_servicos);
        ls_servicos.setOnItemClickListener(this);
        database = Database.getInstance(this);
        domain = domain.getInstance();
    }

    @Override
    protected void onStart() {
        setTitle(getResources().getString(R.string.str_servicos));
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }
        adapter = domain.findServicos(this, database);
        ls_servicos.setAdapter(adapter);
        filter = new ServicoActivity.DataFilter(adapter);
        ed_descricao_servico.addTextChangedListener(filter);
        database.Close();
        super.onStart();
    }

    @Override
    protected void onRestart() {
        ed_descricao_servico.setText("");
        super.onRestart();
    }

    @Override
    public void onClick(View v) {
        Intent it = new Intent(this, ServicoEditActivity.class);
        startActivityForResult(it, 0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Servico servico = adapter.getItem(position);
        Intent it = new Intent(this, ServicoEditActivity.class);
        it.putExtra(p_servico, servico);
        startActivityForResult(it, 0);
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
