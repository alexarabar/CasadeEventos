package br.com.alexandrebarboza.casadeeventos;

import android.content.DialogInterface;
import android.content.Intent;
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

import br.com.alexandrebarboza.casadeeventos.Database.Database;
import br.com.alexandrebarboza.casadeeventos.Domain.Domain;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Cliente;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Contrato;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Email;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Endereco;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Telefone;
import br.com.alexandrebarboza.casadeeventos.Domain.Relation.ClienteXEmail;
import br.com.alexandrebarboza.casadeeventos.Domain.Relation.ClienteXEndereco;
import br.com.alexandrebarboza.casadeeventos.Domain.Relation.ClienteXTelefone;
import br.com.alexandrebarboza.casadeeventos.Utility.Adapters.Adapters;
import br.com.alexandrebarboza.casadeeventos.Utility.Connector;
import br.com.alexandrebarboza.casadeeventos.Utility.Messages.Input;
import br.com.alexandrebarboza.casadeeventos.Utility.Messages.InputContrato;
import br.com.alexandrebarboza.casadeeventos.Utility.Messages.InputEndereco;
import br.com.alexandrebarboza.casadeeventos.Utility.Messages.Output;
import br.com.alexandrebarboza.casadeeventos.Utility.Utility;

public class ClienteEditActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {
    private final short DELETE_CLIENTE  = 1;
    private final short DELETE_TELEFONE = 2;
    private final short UPDATE_TELEFONE = 3;
    private final short INSERT_TELEFONE = 4;
    private final short DELETE_EMAIL = 5;
    private final short UPDATE_EMAIL = 6;
    private final short INSERT_EMAIL = 7;
    private final short DELETE_ENDERECO = 8;
    private final short UPDATE_ENDERECO = 9;
    private final short INSERT_ENDERECO = 10;
    private final short DELETE_CONTRATO = 11;
    private final short UPDATE_CONTRATO = 12;
    private final short INSERT_CONTRATO = 13;

    private short operator;
    private Cliente cliente;
    private Database database;
    private Domain domain;

    private Spinner sp_telefone;
    private ArrayAdapter<String>            ad_telefone;
    private ArrayAdapter <Telefone>         telefones;
    private ArrayAdapter <ClienteXTelefone> cliente_x_telefone;

    private Spinner                      sp_email;
    private ArrayAdapter <String>        ad_email;
    private ArrayAdapter <Email>         emails;
    private ArrayAdapter <ClienteXEmail> cliente_x_email;

    private Spinner                         sp_endereco;
    private ArrayAdapter <String>           ad_endereco;
    private ArrayAdapter <Endereco>         enderecos;
    private ArrayAdapter <ClienteXEndereco> cliente_x_endereco;

    private Spinner                         sp_contrato;
    private ArrayAdapter <String>           ad_contrato;
    private ArrayAdapter <Contrato>         contratos;

    private ImageButton ib_insert_telefone;
    private ImageButton ib_update_telefone;
    private ImageButton ib_delete_telefone;

    private ImageButton ib_insert_email;
    private ImageButton ib_update_email;
    private ImageButton ib_delete_email;

    private ImageButton ib_insert_endereco;
    private ImageButton ib_update_endereco;
    private ImageButton ib_delete_endereco;

    private ImageButton ib_insert_contrato;
    private ImageButton ib_update_contrato;
    private ImageButton ib_delete_contrato;

    private EditText ed_razao_social;
    private EditText ed_cnpj;

    private void Preencher() {
        if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
            return;
        }
        ed_razao_social.setText(cliente.getRazao_social());
        ed_cnpj.setText(cliente.getCnpj());
        if (domain.setClienteTelefones(telefones, cliente_x_telefone, ad_telefone, cliente.get_id())) {
            Utility.resetImages(true, ib_update_telefone, ib_delete_telefone);
            Utility.telefoneSort(telefones);
            Adapters.adapterSort(ad_telefone);
        } else {
            Utility.resetImages(false, ib_update_telefone, ib_delete_telefone);
        }
        if (domain.setClienteEmails(emails, cliente_x_email, ad_email, cliente.get_id())) {
            Utility.resetImages(true, ib_update_email, ib_delete_email);
            Utility.emailSort(emails);
            Adapters.adapterSort(ad_email);
        } else {
            Utility.resetImages(false, ib_update_email, ib_delete_email);
        }
        if (domain.setClienteEnderecos(enderecos, cliente_x_endereco, ad_endereco, cliente.get_id())) {
            Utility.resetImages(true, ib_update_endereco, ib_delete_endereco);
            Utility.enderecoSort(enderecos);
            Adapters.adapterSort(ad_endereco);
        } else {
            Utility.resetImages(false, ib_update_endereco, ib_delete_endereco);
        }
        if (domain.setClienteContratos(contratos, ad_contrato, cliente.get_id())) {
            Utility.resetImages(true, ib_update_contrato, ib_delete_contrato);
            Utility.contratoSort(contratos);
            Adapters.adapterSort(ad_contrato);
        } else {
            Utility.resetImages(false, ib_update_contrato, ib_delete_contrato);
        }
        database.Close();
    }

    private void Salvar() {
        if (!Connector.OpenDatabase(getResources(), this, database, domain, true)) {
            return;
        }
        boolean result  = false;
        int[] control   = {0};
        if (cliente.get_id() == 0) {   // Modo de Inclusão.
            result = Connector.SaveAddCliente(this, domain, cliente, ad_telefone, ad_email, enderecos, ed_razao_social, ed_cnpj);
            control[0] = 1;
        } else {                      // Modo de Alteração.
            result = Connector.SaveUpdateCliente(this, domain, cliente, contratos, telefones, emails, enderecos, ad_telefone, ad_email, sp_telefone, sp_email, ed_razao_social, ed_cnpj, control);
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
        operator = DELETE_CLIENTE;
        String st1, st2, st3;
        st1 = getResources().getString(R.string.str_delete);
        st2 = getResources().getString(R.string.str_clientes);
        st2 = st2.substring(0, st2.length() -1);
        st2 = st2.toLowerCase();
        st3 = ed_razao_social.getText().toString();
        Output.Question(this, st1 + " o " + st2 + " " + st3, getResources().getString(R.string.str_remove));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        operator = 0; // nenhuma operação

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_edit);

        sp_telefone = (Spinner) findViewById(R.id.spinner_telefone_cliente);
        ad_telefone = Adapters.GetSpinner(this, sp_telefone);
        ad_telefone.setNotifyOnChange(true);

        sp_email = (Spinner) findViewById(R.id.spinner_email_cliente);
        ad_email = Adapters.GetSpinner(this, sp_email);
        ad_email.setNotifyOnChange(true);

        sp_endereco = (Spinner) findViewById(R.id.spinner_endereco_cliente);
        ad_endereco = Adapters.GetSpinner(this, sp_endereco);
        ad_endereco.setNotifyOnChange(true);

        sp_contrato = (Spinner) findViewById(R.id.spinner_contrato_cliente);
        ad_contrato = Adapters.GetSpinner(this, sp_contrato);
        ad_contrato.setNotifyOnChange(true);

        ed_razao_social  = (EditText) findViewById(R.id.edit_razao_social_cliente2);
        ed_cnpj = (EditText) findViewById(R.id.edit_cnpj_cliente);

        ib_insert_telefone = (ImageButton) findViewById(R.id.button_adicionar_telefone_cliente);
        ib_update_telefone = (ImageButton) findViewById(R.id.button_alterar_telefone_cliente);
        ib_delete_telefone = (ImageButton) findViewById(R.id.button_excluir_telefone_cliente);

        ib_insert_email = (ImageButton) findViewById(R.id.button_adicionar_email_cliente);
        ib_update_email = (ImageButton) findViewById(R.id.button_alterar_email_cliente);
        ib_delete_email = (ImageButton) findViewById(R.id.button_excluir_email_cliente);

        ib_insert_endereco = (ImageButton) findViewById(R.id.button_adicionar_endereco_cliente);
        ib_update_endereco = (ImageButton) findViewById(R.id.button_alterar_endereco_cliente);
        ib_delete_endereco = (ImageButton) findViewById(R.id.button_excluir_endereco_cliente);

        ib_insert_contrato = (ImageButton) findViewById(R.id.button_adicionar_contrato_cliente);
        ib_update_contrato = (ImageButton) findViewById(R.id.button_alterar_contrato_cliente);
        ib_delete_contrato = (ImageButton) findViewById(R.id.button_excluir_contrato_cliente);

        ib_insert_telefone.setOnClickListener(this);
        ib_update_telefone.setOnClickListener(this);
        ib_delete_telefone.setOnClickListener(this);

        ib_insert_email.setOnClickListener(this);
        ib_update_email.setOnClickListener(this);
        ib_delete_email.setOnClickListener(this);

        ib_insert_endereco.setOnClickListener(this);
        ib_update_endereco.setOnClickListener(this);
        ib_delete_endereco.setOnClickListener(this);

        ib_insert_contrato.setOnClickListener(this);
        ib_update_contrato.setOnClickListener(this);
        ib_delete_contrato.setOnClickListener(this);

        Utility.resetImages(false, ib_update_telefone, ib_delete_telefone);
        Utility.resetImages(false, ib_update_email, ib_delete_email);
        Utility.resetImages(false, ib_update_endereco, ib_delete_endereco);
        Utility.resetImages(false, ib_update_contrato, ib_delete_contrato);

        telefones         = new ArrayAdapter <Telefone> (this, 0);
        cliente_x_telefone = new ArrayAdapter <ClienteXTelefone> (this, 0);

        emails         = new ArrayAdapter <Email> (this, 0);
        cliente_x_email = new ArrayAdapter <ClienteXEmail> (this, 0);

        enderecos         = new ArrayAdapter <Endereco> (this, 0);
        cliente_x_endereco = new ArrayAdapter <ClienteXEndereco> (this, 0);

        contratos         = new ArrayAdapter <Contrato> (this, 0);

        database = Database.getInstance(this);
        domain = domain.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(ClienteActivity.p_cliente)) { // modo de alteração;
            cliente = (Cliente) bundle.getSerializable(ClienteActivity.p_cliente);
            View outer = findViewById(R.id.layout_outer_contrato_cliente);
            outer.setVisibility(View.VISIBLE);
            Preencher();
        } else {
            cliente = new Cliente(); // modo de inclusão.
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle(getResources().getString(R.string.str_cliente_editar));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        if (cliente.get_id() > 0)
            menu.getItem(1).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_salvar:
                int flag = 0;
                int[] vet = {-1};
                String campo = "";
                if (ed_razao_social.getText().toString().trim().isEmpty()) {
                    campo = getResources().getString(R.string.str_razao_social);
                } else if (ed_cnpj.getText().toString().trim().isEmpty()) {
                    campo = getResources().getString(R.string.str_cnpj);
                } else {
                    if (!Connector.OpenDatabase(getResources(), this, database, domain, false)) {
                        return false;
                    }
                    if (cliente.get_id() == 0) { // Inclusão.
                        flag = Connector.LoadAddCliente(domain, sp_telefone, sp_email, enderecos, ed_razao_social, ed_cnpj, vet);
                    } else {                    // Alteração.
                        // TODO já existe razão social erro!
                        flag = Connector.LoadUpddateCliente(domain, cliente, contratos, telefones, emails, enderecos, sp_telefone, sp_email, ed_razao_social, ed_cnpj, vet);
                    }
                    database.Close();
                }
                String msg, s;
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
                    case 2: // Já existe um registro com essa razão social.
                        s = getResources().getString(R.string.str_razao_social);
                        s = s.substring(0, s.length() -1).toLowerCase();
                        msg = "A " + s + " " + getResources().getString(R.string.str_found) + " " + getResources().getString(R.string.str_in_db);
                        Output.Info(this, getResources().getString(R.string.str_info_repeat), msg);
                        break;
                    case 3: // Já existe um registro com esse CNPJ
                        msg = "A " + getResources().getString(R.string.str_key_cliente) + " " + getResources().getString(R.string.str_found) + " " + getResources().getString(R.string.str_in_db);
                        Output.Info(this, getResources().getString(R.string.str_info_repeat), msg);
                        break;
                    case 4: // Já existe um registro com esse numero de telefone.
                        msg = "O " + getResources().getString(R.string.str_key_telefone) + " " + sp_telefone.getItemAtPosition(pos) + " " + getResources().getString(R.string.str_found) + " " + getResources().getString(R.string.str_in_db);
                        Output.Info(this, getResources().getString(R.string.str_info_repeat), msg);
                        break;
                    case 5: // Já existe um usuário com esse endereço de e-mail.
                        msg = "O " + getResources().getString(R.string.str_key_email) + " " + sp_email.getItemAtPosition(pos) + " " + getResources().getString(R.string.str_found) + " " + getResources().getString(R.string.str_in_db);
                        Output.Info(this, getResources().getString(R.string.str_info_repeat), msg);
                        break;
                    case 6: // Já existe um usuário com exatamente este endereço.
                        s = getResources().getString(R.string.str_endereco);
                        s = s.substring(0, s.length() -1).toLowerCase() + " ";
                        msg = "O " + s + sp_endereco.getItemAtPosition(pos) + " " + getResources().getString(R.string.str_found) + " " +  getResources().getString(R.string.str_in_db);
                        Output.Info(this, getResources().getString(R.string.str_info_repeat), msg);
                        break;
                    case 7: // Já existe um contrato nesta data/hora.
                        s = getResources().getString(R.string.str_found_time);
                        s = s.substring(0, s.length() -1) + " ";
                        msg = s + sp_contrato.getItemAtPosition(pos);
                        Output.Info(this, getResources().getString(R.string.str_info_repeat), msg);
                        break;
                }
                break;
            case R.id.m_excluir:
                Excluir();
                break;
            case R.id.m_limpar:
                ad_telefone.clear();
                ad_email.clear();
                ad_endereco.clear();
                telefones.clear();
                emails.clear();
                enderecos.clear();
                if (cliente.get_id() == 0) {
                    ed_razao_social.setText("");
                    ed_cnpj.setText("");
                } else {
                    contratos.clear();
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
    public void onClick(View v) {
        Input input;
        InputEndereco input_endereco;
        InputContrato input_contrato;
        String st1, st2, st3;
        Bundle bundle;
        switch (v.getId()) {
            case R.id.button_adicionar_telefone_cliente:
                operator = INSERT_TELEFONE;
                input = new Input(this, getResources().getString(R.string.str_telefone), "", 15);
                input.setInput();
                break;
            case R.id.button_alterar_telefone_cliente:
                operator = UPDATE_TELEFONE;
                input = new Input(this, getResources().getString(R.string.str_telefone), sp_telefone.getSelectedItem().toString(), 15);
                input.setInput();
                break;
            case R.id.button_excluir_telefone_cliente:
                operator = DELETE_TELEFONE;
                st1 = getResources().getString(R.string.str_delete);
                st2 = getResources().getString(R.string.str_telefone);
                st2 = st2.substring(0, st2.length() - 1).toLowerCase();
                st3 = sp_telefone.getSelectedItem().toString();
                Output.Question(this, st1 + " o " + st2 + " " + st3 + "?", getResources().getString(R.string.str_remove));
                break;
            case R.id.button_adicionar_email_cliente:
                operator = INSERT_EMAIL;
                input = new Input(this, getResources().getString(R.string.str_email), "", 65);
                input.setInput();
                break;
            case R.id.button_alterar_email_cliente:
                operator = UPDATE_EMAIL;
                input = new Input(this, getResources().getString(R.string.str_email), sp_email.getSelectedItem().toString(), 65);
                input.setInput();
                break;
            case R.id.button_excluir_email_cliente:
                operator = DELETE_EMAIL;
                st1 = getResources().getString(R.string.str_delete);
                st2 = getResources().getString(R.string.str_email);
                st2 = st2.substring(0, st2.length() - 1).toLowerCase();
                st3 = sp_email.getSelectedItem().toString();
                Output.Question(this, st1 + " o " + st2 + " " + st3 + "?", getResources().getString(R.string.str_remove));
                break;
            case R.id.button_adicionar_endereco_cliente:
                operator = INSERT_ENDERECO;
                input_endereco = new InputEndereco(this, getResources().getString(R.string.str_endereco), null);
                input_endereco.setInputEndereco();
                break;
            case R.id.button_alterar_endereco_cliente:
                operator = UPDATE_ENDERECO;
                String[] array_e = {"", "", "", "", "", "", ""};
                Endereco endereco = enderecos.getItem(sp_endereco.getSelectedItemPosition());
                Utility.enderecoToArray(array_e, endereco);
                input_endereco = new InputEndereco(this, getResources().getString(R.string.str_endereco), array_e);
                input_endereco.setInputEndereco();
                break;
            case R.id.button_excluir_endereco_cliente:
                operator = DELETE_ENDERECO;
                st1 = getResources().getString(R.string.str_delete);
                st2 = getResources().getString(R.string.str_endereco);
                st2 = st2.substring(0, st2.length() - 1).toLowerCase();
                st3 = sp_endereco.getSelectedItem().toString();
                Output.Question(this, st1 + " o " + st2 + " " + st3 + "?", getResources().getString(R.string.str_remove));
                break;
            case R.id.button_adicionar_contrato_cliente:
                operator = INSERT_CONTRATO;
                input_contrato = new InputContrato(this, getResources().getString(R.string.str_contrato), null);
                input_contrato.setInputContrato();
                break;
            case R.id.button_alterar_contrato_cliente:
                operator = UPDATE_CONTRATO;
                String[] array_c = {"", "", "", "", "", ""};
                Contrato contrato = contratos.getItem(sp_contrato.getSelectedItemPosition());
                Utility.contratoToArray(array_c, contrato);
                input_contrato = new InputContrato(this, getResources().getString(R.string.str_contrato), array_c);
                input_contrato.setInputContrato();
                break;
            case R.id.button_excluir_contrato_cliente:
                operator = DELETE_CONTRATO;
                st1 = getResources().getString(R.string.str_delete);
                st2 = getResources().getString(R.string.str_contrato);
                st2 = st2.substring(0, st2.length() - 1).toLowerCase();
                st3 = sp_contrato.getSelectedItem().toString();
                Output.Question(this, st1 + " o " + st2 + " " + st3 + "?", getResources().getString(R.string.str_remove));
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Intent intent = null;
        ArrayAdapter <String> updated = null;
        Spinner selected = null;
        String   found = getResources().getString(R.string.str_found) + "!";
        String   str = "";
        String[] vec = {""};
        switch (operator) {
            case DELETE_CLIENTE: // Remover Cliente
                if (!Connector.OpenDatabase(getResources(), this, database, domain, true)) {
                    return;
                }
                String[] v = {""};
                boolean flag = Connector.DeleteCliente(this, domain, cliente, contratos, telefones, emails, enderecos, v);
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
                break;
            case DELETE_TELEFONE: // Remover Telefone
                Utility.deleteData(sp_telefone, ad_telefone, ib_update_telefone, ib_delete_telefone);
                break;
            case UPDATE_TELEFONE: // Alterar Telefone
                intent = getIntent();
                str = intent.getStringExtra("INPUT");
                if (Utility.updateData(this, str, sp_telefone, ad_telefone, getResources().getString(R.string.str_telefone), found)) {
                    updated  = ad_telefone;
                    selected = sp_telefone;
                }
                break;
            case INSERT_TELEFONE: // Incluir Telefone
                intent = getIntent();
                str = intent.getStringExtra("INPUT");
                if (Utility.insertData(this, str, ad_telefone, ib_update_telefone, ib_delete_telefone, getResources().getString(R.string.str_telefone), found, getResources().getString(R.string.str_empty))) {
                    updated  = ad_telefone;
                    selected = sp_telefone;
                }
                break;
            case DELETE_EMAIL: // Excluir e-mail
                Utility.deleteData(sp_email, ad_email, ib_update_email, ib_delete_email);
                break;
            case UPDATE_EMAIL: // Alterar e-mail.
                intent = getIntent();
                str = intent.getStringExtra("INPUT");
                if (Utility.updateData(this, str, sp_email, ad_email, getResources().getString(R.string.str_email), found)) {
                    updated  = ad_email;
                    selected = sp_email;
                }
                break;
            case INSERT_EMAIL: // Incluir e-mail.
                intent = getIntent();
                str = intent.getStringExtra("INPUT");
                if (Utility.insertData(this, str, ad_email, ib_update_email, ib_delete_email, getResources().getString(R.string.str_email), found, getResources().getString(R.string.str_empty))) {
                    updated  = ad_email;
                    selected = sp_email;
                }
                break;
            case DELETE_ENDERECO: // Excluir endereço.
                Utility.deleteEndereco(sp_endereco, ad_endereco, enderecos, ib_update_endereco, ib_delete_endereco);
                break;
            case UPDATE_ENDERECO: // Alterar endereço.
                intent = getIntent();
                if (Utility.updateEndereco(this, intent, sp_endereco, ad_endereco,  enderecos, getResources().getString(R.string.str_endereco), found, vec)) {
                    str = vec[0];
                    updated = ad_endereco;
                    selected = sp_endereco;
                }
                break;
            case INSERT_ENDERECO: // Incluir endereço.
                intent = getIntent();
                if (Utility.insertEndereco(this, intent, ad_endereco, enderecos, ib_update_endereco, ib_delete_endereco, getResources().getString(R.string.str_endereco), found, vec)) {
                    str = vec[0];
                    updated  = ad_endereco;
                    selected = sp_endereco;
                }
                break;
            case DELETE_CONTRATO: // Excluir contrato.
                Utility.deleteContrato(sp_contrato, ad_contrato, contratos, ib_update_contrato, ib_delete_contrato);
                break;
            case UPDATE_CONTRATO: // Alterar contrato.
                intent = getIntent();
                if (Utility.updateContrato(this, intent, sp_contrato, ad_contrato,  contratos, cliente.get_id(), vec)) {
                    str = vec[0];
                    updated  = ad_contrato;
                    selected = sp_contrato;
                }
                break;
            case INSERT_CONTRATO: // Incluir contrato.
                intent = getIntent();
                if (Utility.insertContrato(this, intent, ad_contrato, contratos, ib_update_contrato, ib_delete_contrato, cliente.get_id(), vec)) {
                    str = vec[0];
                    updated  = ad_contrato;
                    selected = sp_contrato;
                }
                break;

        }
        if (updated != null) {
            if (operator == INSERT_ENDERECO || operator == UPDATE_ENDERECO) {
                Utility.enderecoSort(enderecos);
            }
            if (operator == INSERT_CONTRATO || operator == UPDATE_CONTRATO) {
                Utility.contratoSort(contratos);
            }
            Adapters.adapterSort(updated);
            updated.notifyDataSetChanged();
            if (selected != null) {
                selected.setSelection(updated.getPosition(str));
            }
            if (intent != null) {
                if (operator == INSERT_ENDERECO || operator == UPDATE_ENDERECO) {
                    Utility.enderecoClear(intent);
                } else if (operator == INSERT_CONTRATO || operator == UPDATE_CONTRATO) {
                    Utility.contratoClear(intent);
                } else {
                    intent.removeExtra("INPUT");
                }
            }
        }
        operator = 0;
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }
}
