package br.com.alexandrebarboza.casadeeventos;

import android.app.Activity;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import br.com.alexandrebarboza.casadeeventos.Database.Database;
import br.com.alexandrebarboza.casadeeventos.Domain.Domain;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Servico;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Tipo;
import br.com.alexandrebarboza.casadeeventos.Utility.Adapters.Adapters;
import br.com.alexandrebarboza.casadeeventos.Utility.Adapters.EventoServicoAdapter;
import br.com.alexandrebarboza.casadeeventos.Utility.Connector;

public class EventoServicosActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener, AdapterView.OnClickListener {
    private Database database;
    private Domain domain;
    private ArrayAdapter<String>  ad_tipo;
    private EventoServicoAdapter  ad_servicos;
    private Spinner sp_tipo;
    private ListView ls_servicos;
    private Button bt_add;
    private Button bt_cancel;

    private void getServicos() {
        if (!sp_tipo.isSelected()) {
            return;
        }
        String str = sp_tipo.getSelectedItem().toString();
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }
        long id_tipo = domain.getIdTipo(this, str);
        ad_servicos = domain.findServicos(this, id_tipo);
        ls_servicos.setAdapter(ad_servicos);
        database.Close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_servicos);
        sp_tipo = (Spinner) findViewById(R.id.spinner_evento_servicos);
        sp_tipo.setOnItemSelectedListener(this);
        ad_tipo = Adapters.GetSpinner(this, sp_tipo);
        ad_tipo.setNotifyOnChange(true);
        ls_servicos = (ListView) findViewById(R.id.list_evento_servicos);
        bt_add = (Button) findViewById(R.id.button_evento_servicos_ok);
        bt_add.setOnClickListener(this);
        bt_cancel = (Button) findViewById(R.id.button_evento_servicos_cancel);
        bt_cancel.setOnClickListener(this);
        database = Database.getInstance(this);
        domain = domain.getInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();
        setTitle(getResources().getString(R.string.str_servicos));
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }
        domain.selectAllTipos(ad_tipo);
        ad_tipo.notifyDataSetChanged();
        database.Close();
        if (sp_tipo.getCount() > 0) {
            sp_tipo.setSelection(0);
            sp_tipo.setSelected(true);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        getServicos();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_evento_servicos_ok:
                if (ad_servicos != null) {
                    ArrayList array_list = ad_servicos.getArrayList();
                    getIntent().putStringArrayListExtra("LIST_SERVICOS", array_list);
                    setResult(Activity.RESULT_OK, getIntent());
                }
                finish();
                break;
            case R.id.button_evento_servicos_cancel:
                setResult(Activity.RESULT_CANCELED, getIntent());
                finish();
                break;
        }
    }
}
