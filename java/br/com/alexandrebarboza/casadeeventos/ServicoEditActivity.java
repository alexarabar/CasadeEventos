package br.com.alexandrebarboza.casadeeventos;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

import br.com.alexandrebarboza.casadeeventos.Domain.Domain;
import br.com.alexandrebarboza.casadeeventos.Database.Database;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Servico;
import br.com.alexandrebarboza.casadeeventos.Utility.Connector;
import br.com.alexandrebarboza.casadeeventos.Utility.Messages.Output;
import br.com.alexandrebarboza.casadeeventos.Utility.Utility;

public class ServicoEditActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {
    private final short DELETE_SERVICO = 1;
    private final int   REQUEST_TIPO   = 1;
    private Database    database;
    private Domain      domain;
    private DataFilter  filter;
    private ImageButton ib_tipo;
    private Intent      it_tipo;
    private EditText ed_servico_valor;
    private EditText ed_servico_tipo;
    private EditText ed_servico_descricao;
    private Servico  servico;
    private short operator;

    private class DataFilter implements TextWatcher {
        private final Locale locale = new Locale("pt", "BR");
        private String current;

        public DataFilter() {
            current = null;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals(current) && !s.toString().isEmpty()) {
                ed_servico_valor.removeTextChangedListener(this);
                String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance(locale).getCurrency().getSymbol());
                String cleanString = s.toString().replaceAll(replaceable, "");
                double parsed;
                try {
                    parsed = Double.parseDouble(cleanString);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    parsed = 0.00;
                }
                if (parsed == 0) {
                    ed_servico_valor.setText("");
                    ed_servico_valor.setSelection(0);
                    ed_servico_valor.addTextChangedListener(this);
                    return;
                }
                String formatted = NumberFormat.getCurrencyInstance().format((parsed / 100));
                current = Utility.convertCurrencyForBR(formatted);

                /*
                System.out.println("*** CURRENT: " + current + " ***");
                double t = Utility.getDoubleOfCurrencyForBR(current);
                System.out.println("*** DOUBLE:  " + t + " ***");
                System.out.println("*** FORMAT:  " + Utility.getDoubleFormatForUS(t, true) + " ***");
                double t100 = t * 100;
                System.out.println("*** (100):  " + t100 + " ***");
                System.out.println("*** (100) FORMAT:  " + Utility.getDoubleFormatForUS(t100, false) + " ***");
                */

                ed_servico_valor.setText(current);
                ed_servico_valor.setSelection(current.length());
                ed_servico_valor.addTextChangedListener(this);
            }
        }
    }
    private void Preencher() {
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }
        ed_servico_descricao.setText(servico.getDescricao());
        double dbl = servico.getValor();
        String str = Utility.getDoubleFormatForUS(dbl, true);
        str = Utility.convertCurrencyForBR(str);
        ed_servico_valor.setText(str);
        ed_servico_tipo.setText(domain.getTextTipo(this, servico.get_tipo()));
        database.Close();
    }

    private void Salvar() {
        if (!Connector.OpenDatabase(getResources(), this, database, domain, true)) {
            return;
        }
        boolean result  = false;
        int[] control   = {0};
        String s_tipo   = ed_servico_tipo.getText().toString();
        String s_descricao = ed_servico_descricao.getText().toString();
        String s_valor = ed_servico_valor.getText().toString();
        long id_tipo    = Connector.getIdTipo(this, domain, s_tipo);
        if (servico.get_id() == 0) {   // Modo de Inclusão.
            result = Connector.SaveAddServico(this, domain, servico, id_tipo, s_descricao, s_valor);
            control[0] = 1;
        } else {                      // Modo de Alteração.
            result = Connector.SaveUpdateServico(this, domain, servico, id_tipo, s_descricao, s_valor, control);
        }
        if (result) {
            if (control[0] == 1) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_success), Toast.LENGTH_SHORT).show();
            }
            finish();
        }
        database.Close();
    }
    private void Excluir() {
        operator = DELETE_SERVICO;
        String st1, st2, st3;
        st1 = getResources().getString(R.string.str_delete);
        st2 = getResources().getString(R.string.str_servicos);
        st2 = st2.substring(0, st2.length() -1);
        st2 = st2.toLowerCase();
        st3 = ed_servico_descricao.getText().toString();
        Output.Question(this, st1 + " o " + st2 + " " + st3, getResources().getString(R.string.str_remove));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        operator = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servico_edit);
        ed_servico_tipo = (EditText) findViewById(R.id.edit_tipo_servico);
        ed_servico_descricao = (EditText) findViewById(R.id.edit_descricao_servico2);
        ed_servico_valor = (EditText) findViewById(R.id.edit_valor_servico);
        ib_tipo = (ImageButton) findViewById(R.id.button_procurar_tipo_servico);
        ed_servico_tipo.setKeyListener(null); // Desabilita digitação.
        ib_tipo.setOnClickListener(this);
        filter = new DataFilter();
        ed_servico_valor.addTextChangedListener(filter);
        database = Database.getInstance(this);
        domain = domain.getInstance();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(ServicoActivity.p_servico)) { // modo de alteração;
            servico = (Servico) bundle.getSerializable(ServicoActivity.p_servico);
            Preencher();
        } else {
            servico = new Servico(); // modo de inclusão.
        }
    }

    @Override
    protected void onStart() {
        setTitle(getResources().getString(R.string.str_servico_editar));
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_salvar:
                int flag = 0;
                int[] vet = {-1};
                String s_tipo = ed_servico_tipo.getText().toString();
                String s_descricao = ed_servico_descricao.getText().toString();
                String s_valor = ed_servico_valor.getText().toString();
                String campo = "";
                if (s_tipo.isEmpty()) {
                    campo = getResources().getString(R.string.str_tipo);
                } else if (s_descricao.isEmpty()) {
                    campo = getResources().getString(R.string.str_descricao);
                } else {
                    if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
                        return false;
                    }
                    long tipo_id = domain.getIdTipo(this, s_tipo);
                    if (servico.get_id() == 0) { // Inclusão.
                        flag = Connector.LoadAddServico(domain, tipo_id, s_descricao, vet);
                    } else {                    // Alteração.
                        flag = Connector.LoadUpdateServico(domain, servico, tipo_id, s_descricao, vet);
                    }
                    database.Close();
                }
                String msg;
                int pos = vet[0];
                switch (flag) {
                    case -1:  // Erro na consulta SQL.
                        msg = getResources().getString(R.string.str_err_sql_query) + " " + getResources().getString(R.string.str_msg_plus) + "\n" + domain.getError();
                        Output.Alert(this, getResources().getString(R.string.str_fail), msg);
                        break;
                    case 0:   // Erro no preenchimento do Campo
                        campo = " " + campo.substring(0, campo.length() - 1) + "!";
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_fill) + campo, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Salvar();
                        break;
                    case 2: // Já existe um registro com essa descrição.
                        String s = getResources().getString(R.string.str_descricao);
                        s = s.substring(0, s.length() -1).toLowerCase();
                        msg = "A " + s + " " + getResources().getString(R.string.str_found) + " " + getResources().getString(R.string.str_in_db);
                        Output.Info(this, getResources().getString(R.string.str_info_repeat), msg);
                        break;
                }
                break;
            case R.id.m_excluir:
                Excluir();
                break;
            case R.id.m_limpar:
                if (servico.get_id() == 0) {
                    ed_servico_tipo.setText("");
                    ed_servico_descricao.setText("");
                    ed_servico_valor.setText("");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        if (servico.get_id() > 0)
            menu.getItem(1).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_procurar_tipo_servico:
                it_tipo = new Intent(this, TipoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("INPUT", ed_servico_tipo.getText().toString());
                it_tipo.putExtras(bundle);
                startActivityForResult(it_tipo, REQUEST_TIPO, bundle);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (requestCode == REQUEST_TIPO) {
                    String txt = bundle.getString("INPUT");
                    it_tipo.replaceExtras(bundle);
                    ed_servico_tipo.setText(txt);
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (data != null) {
                if (data.hasExtra("ACTION") == true) {
                    data.removeExtra("ACTION");
                }
                if (data.hasExtra("DEL") == true) {
                    data.removeExtra("DEL");
                }
                if (data.hasExtra("OLD") == true) {
                    data.removeExtra("OLD");
                }
                if (data.hasExtra("INPUT") == true) {
                    data.removeExtra("INPUT");
                }
                if (requestCode == REQUEST_TIPO) {
                    it_tipo.replaceExtras(data);
                }
            }
            long id;
            String txt;
            if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
                return;
            }
            if (requestCode == REQUEST_TIPO) {
                txt = ed_servico_tipo.getText().toString();
                id = domain.getIdTipo(this, txt);
                if (id == 0) {
                    ed_servico_tipo.setText("");
                }
            }
            database.Close();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        String   str = "";
        String[] vec = {""};
        switch (operator) {
            case DELETE_SERVICO: // Remover Serviço
                if (!Connector.OpenDatabase(getResources(), this, database, domain, true)) {
                    return;
                }
                String[] v = {""};
                boolean flag = Connector.DeleteServico(this, domain, servico, v);
                str = v[0];
                if (!str.isEmpty()) {
                    String msg = getResources().getString(R.string.str_err_delete) + " " + str.toLowerCase() + getResources().getString(R.string.str_msg_plus) + "\n" + domain.getError();
                    Output.Alert(this, getResources().getString(R.string.str_fail), msg);
                } else {
                    if (flag) {
                        Toast.makeText(this, getResources().getString(R.string.str_success), Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
                database.Close();
                break;
        }
        operator = 0;
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

}
