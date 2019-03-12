package br.com.alexandrebarboza.casadeeventos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
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
import br.com.alexandrebarboza.casadeeventos.Utility.Connector;
import br.com.alexandrebarboza.casadeeventos.Utility.Messages.Output;
import br.com.alexandrebarboza.casadeeventos.Utility.Messages.ProgressClientes;
import br.com.alexandrebarboza.casadeeventos.Utility.Utility;

public class ClienteActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {
    private Database database;
    private Domain   domain;
    private DataFilter  filter;
    private EditText    ed_razao_social;
    private ImageButton bt_add;
    private ListView    ls_clientes;
    private ArrayAdapter<Cliente> adapter;
    ProgressClientes progress;
    public static final String p_cliente = "CLIENTE";

    private class DataFilter implements TextWatcher {
        private ArrayAdapter<Cliente> adapter;

        private DataFilter(ArrayAdapter<Cliente> adapter) {
            this.adapter = adapter;
        }

        public void setAdapter(ArrayAdapter<Cliente> adapter) {
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
        setContentView(R.layout.activity_cliente);
        bt_add = (ImageButton) findViewById(R.id.button_adicionar_cliente);
        bt_add.setOnClickListener(this);
        ed_razao_social = (EditText) findViewById(R.id.edit_razao_social_cliente1);
        ls_clientes = (ListView) findViewById(R.id.list_clientes);
        ls_clientes.setOnItemClickListener(this);
        database = Database.getInstance(this);
        domain = domain.getInstance();
        progress = null;
    }

    @Override
    protected void onStart() {
        setTitle(getResources().getString(R.string.str_clientes));
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }
        adapter = domain.findClientes(this, true);
        ls_clientes.setAdapter(adapter);
        filter = new DataFilter(adapter);
        ed_razao_social.addTextChangedListener(filter);
        database.Close();
        super.onStart();
    }

    @Override
    protected void onRestart() {
        ed_razao_social.setText("");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        if (progress != null && progress.getStatus() == AsyncTask.Status.RUNNING) {
            progress.cancel(false);
            progress = null;
        }
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        Intent it = new Intent(this, ClienteEditActivity.class);
        startActivityForResult(it, 0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cliente cliente = adapter.getItem(position);
        Intent it = new Intent(this, ClienteEditActivity.class);
        it.putExtra(p_cliente, cliente);
        startActivityForResult(it, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        menu.getItem(0).setVisible(false);
        menu.getItem(2).setVisible(false);
        menu.getItem(3).setVisible(false);
        menu.getItem(4).setVisible(true);
        menu.getItem(5).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_voltar:
                setResult(Activity.RESULT_CANCELED, null);
                finish();
                break;
            case R.id.m_exportar:
                int i = ls_clientes.getCount();
                if (i == 0) {
                    break;
                }
                String msg = getResources().getString(R.string.str_export);
                String s = getResources().getString(R.string.str_clientes).toLowerCase();
                if (i == 1) {
                    s = s.substring(0, s.length() - 1);
                } else {
                    msg += " os";
                }
                msg += " (" + i + ") " + s + " " + getResources().getString(R.string.str_contacts) + "?";
                Output.Question(this, msg, getResources().getString(R.string.str_exp_title));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }

        // TODO

        System.out.println("*** DISMISS! ***");

        ProgressDialog status = Utility.createProgressDialog(this, this.getResources().getString(R.string.str_contact_create), ls_clientes.getCount());
        progress = new ProgressClientes(database, domain, this, status, ls_clientes);
        progress.execute();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
    }
}
