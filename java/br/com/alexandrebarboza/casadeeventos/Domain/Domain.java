package br.com.alexandrebarboza.casadeeventos.Domain;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.widget.ArrayAdapter;

import br.com.alexandrebarboza.casadeeventos.Database.Database;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Cliente;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Contrato;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Email;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Endereco;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Evento;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Pagamento;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Servico;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Telefone;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Tipo;
import br.com.alexandrebarboza.casadeeventos.Domain.Relation.ClienteXEmail;
import br.com.alexandrebarboza.casadeeventos.Domain.Relation.ClienteXEndereco;
import br.com.alexandrebarboza.casadeeventos.Domain.Relation.ClienteXTelefone;
import br.com.alexandrebarboza.casadeeventos.Domain.Relation.EventoXServico;
import br.com.alexandrebarboza.casadeeventos.R;
import br.com.alexandrebarboza.casadeeventos.Utility.Adapters.ClienteAdapter;
import br.com.alexandrebarboza.casadeeventos.Utility.Adapters.ContratoAdapter;
import br.com.alexandrebarboza.casadeeventos.Utility.Adapters.EventoAdapter;
import br.com.alexandrebarboza.casadeeventos.Utility.Adapters.EventoClienteAdapter;
import br.com.alexandrebarboza.casadeeventos.Utility.Adapters.EventoServicoAdapter;
import br.com.alexandrebarboza.casadeeventos.Utility.Adapters.ServicoAdapter;
import br.com.alexandrebarboza.casadeeventos.Utility.Adapters.TipoAdapter;
import br.com.alexandrebarboza.casadeeventos.Utility.Dates;
import br.com.alexandrebarboza.casadeeventos.Utility.Utility;

/**
 * Created by Alexandre on 11/04/2017.
 */

public class Domain {
    private static Domain instance = null;
    private SQLiteDatabase connection = null;
    private String error;

    private Domain() { // Singleton
    }

    private int getRecordFound(String tabela, String columns[], String where) {
        try {
            Cursor c = connection.query(tabela, columns, where, null, null, null, null);
            if (c.getCount() > 0)
                return 1;
            else
                return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
            return -1;
        }
    }

    private long getRecordId(String tabela, String columns[], String where) {
        try {
            long res = 0;
            Cursor c = connection.query(tabela, columns, where, null, null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                res = c.getLong(0);
            }
            c.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
            return -1;
        }
    }

    private Cursor getRecords(String tabela, String columns[], String where, String[] args, String order) {
        try {
            Cursor c = connection.query(true, tabela, columns, where, args, null, null, order, null);
            return c;
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
            return null;
        }
    }

    private ContentValues putCliente(Cliente cliente) {
        ContentValues values = new ContentValues();
        values.put(Cliente.RAZAO_SOCIAL, cliente.getRazao_social());
        values.put(Cliente.CNPJ, cliente.getCnpj());
        return values;
    }

    private ContentValues putEvento(Evento evento) {
        ContentValues values = new ContentValues();
        values.put(Evento._CONTRATO, evento.get_contrato());
        values.put(Evento.DESCRICAO, evento.getDescricao());
        return values;
    }

    private ContentValues putPagamento(Pagamento pagamento) {
        ContentValues values = new ContentValues();
        values.put(Pagamento._EVENTO, pagamento.get_evento());
        values.put(Pagamento.TOTAL, pagamento.getTotal());
        values.put(Pagamento.DESCONTO, pagamento.getDesconto());
        return values;
    }

    private ContentValues putTelefone(Telefone telefone) {
        ContentValues values = new ContentValues();
        values.put(Telefone.NUMERO, telefone.getNumero());
        return values;
    }

    private ContentValues putEventoXServico(EventoXServico evento_x_xervico) {
        ContentValues values = new ContentValues();
        values.put(EventoXServico._EVENTO,  evento_x_xervico.get_evento());
        values.put(EventoXServico._SERVICO, evento_x_xervico.get_servico());
        return values;
    }

    private ContentValues putClienteXTelefone(ClienteXTelefone cliente_x_telefone) {
        ContentValues values = new ContentValues();
        values.put(ClienteXTelefone._CLIENTE, cliente_x_telefone.get_cliente());
        values.put(ClienteXTelefone._TELEFONE, cliente_x_telefone.get_telefone());
        return values;
    }

    private ContentValues putEmail(Email email) {
        ContentValues values = new ContentValues();
        values.put(Email.ENDERECO, email.getEndereco());
        return values;
    }

    private ContentValues putClienteXEmail(ClienteXEmail cliente_x_email) {
        ContentValues values = new ContentValues();
        values.put(ClienteXEmail._CLIENTE, cliente_x_email.get_cliente());
        values.put(ClienteXEmail._EMAIL, cliente_x_email.get_email());
        return values;
    }

    private ContentValues putEndereco(Endereco endereco) {
        ContentValues values = new ContentValues();
        values.put(Endereco.LOGRADOURO, endereco.getLogradouro());
        values.put(Endereco.NUMERO, endereco.getNumero());
        values.put(Endereco.COMPLEMENTO, endereco.getComplemento());
        values.put(Endereco.BAIRRO, endereco.getBairro());
        values.put(Endereco.CIDADE, endereco.getCidade());
        values.put(Endereco.UF, endereco.getUf());
        values.put(Endereco.CEP, endereco.getCep());
        return values;
    }

    private ContentValues putContrato(Contrato contrato) {
        ContentValues values = new ContentValues();
        values.put(Contrato._CLIENTE, contrato.get_cliente());
        values.put(Contrato.DATA_INICIAL, Dates.getDefaultFormatFromSQLDateTime(contrato.getData_inicial()));
        values.put(Contrato.DATA_FINAL, Dates.getDefaultFormatFromSQLDateTime(contrato.getData_final()));
        return values;
    }

    private ContentValues putClienteXEndereco(ClienteXEndereco cliente_x_endereco) {
        ContentValues values = new ContentValues();
        values.put(ClienteXEndereco._CLIENTE,  cliente_x_endereco.get_cliente());
        values.put(ClienteXEndereco._ENDERECO, cliente_x_endereco.get_endereco());
        return values;
    }

    private ContentValues putServico(Servico servico) {
        ContentValues values = new ContentValues();
        values.put(servico._TIPO, servico.get_tipo());
        values.put(Servico.DESCRICAO, servico.getDescricao());
        values.put(Servico.VALOR, servico.getValor());
        return values;
    }

    private ContentValues putTipoServico(Tipo tipo) {
        ContentValues values = new ContentValues();
        values.put(tipo.DESCRICAO, tipo.getDescricao());
        return values;
    }

    private long insertRecord(String tabela, ContentValues values) {
        long result = -1;
        try {
            result = connection.insertOrThrow(tabela, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
        }

        System.out.println("Resultado da Inclusão na tabela: " + tabela + " foi: " + result);

        return result;
    }

    private int updateRecord(String tabela, ContentValues values, long id) {
        int result = -1;
        try {
            result = connection.update(tabela, values, "_id = ?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
        }

        System.out.println("Resultado da Atualização na tabela: " + tabela + " foi: " + result);

        return result;
    }

    private int deleteRecord(String tabela, long id) {
        int result = -1;
        try {
            result = connection.delete(tabela, "_id = ?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
        }

        System.out.println("Resultado da Exclusão na tabela: " + tabela + " foi: " + result);

        return result;
    }

    private int deleteRecord(String tabela, long id, String key) {
        int result = -1;
        try {
            result = connection.delete(tabela, key + " = ?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
        }

        System.out.println("Resultado da Exclusão na tabela: " + tabela + " foi: " + result);

        return result;
    }

    private int deleteRecord(String tabela, long id1, long id2, String key1, String key2) {
        int result = -1;
        try {
            result = connection.delete(tabela, key1 + " = ? AND " + key2 + " = ?", new String[] {String.valueOf(id1), String.valueOf(id2)});
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
        }

        System.out.println("Resultado da Exclusão na tabela: " + tabela + " foi: " + result);

        return result;
    }

    private String makePlaceholders(int len) {
        if (len < 1) {
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }

    private Endereco getEndereco(Cursor cursor, boolean flag) {
        int i = 0;
        Endereco endereco = new Endereco();
        if (flag == true) {
            endereco.set_id(cursor.getLong(i++));
        }
        endereco.setLogradouro(cursor.getString(i++));
        endereco.setNumero(cursor.getInt(i++));
        endereco.setComplemento(cursor.getString(i++));
        endereco.setBairro(cursor.getString(i++));
        endereco.setCidade(cursor.getString(i++));
        endereco.setUf(cursor.getString(i++));
        endereco.setCep(cursor.getInt(i++));
        return endereco;
    }

    private Contrato getContrato(Cursor cursor, boolean flag) {
        int i = 0;
        Contrato contrato = new Contrato();
        if (flag == true) {
            contrato.set_id(cursor.getLong(i++));
        }
        contrato.set_cliente(cursor.getLong(i++));
        contrato.setData_inicial(Dates.getSQLDateTime(cursor.getString(i++)));
        contrato.setData_final(Dates.getSQLDateTime(cursor.getString(i++)));
        return contrato;
    }

    private void setServicos(ArrayAdapter <Servico> servicos, ArrayAdapter adapter, long id) {
        String where = "_id = '" + id + "'";
        String col[] = {"_id", "descricao"};
        Cursor cursor = getRecords(Servico.TABELA, col, where, null, "descricao COLLATE NOCASE ASC");
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    Servico serv = new Servico();
                    serv.set_id(cursor.getLong(0));
                    serv.setDescricao(cursor.getString(1));
                    servicos.add(serv);
                    adapter.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    private void setTelefones(ArrayAdapter <Telefone> telefones, ArrayAdapter adapter, long id) {
        String where = "_id = '" + id + "'";
        String col[] = {"_id", "numero"};
        Cursor cursor = getRecords(Telefone.TABELA, col, where, null, "numero COLLATE NOCASE ASC");
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    Telefone tel = new Telefone();
                    tel.set_id(cursor.getLong(0));
                    tel.setNumero(cursor.getString(1));
                    telefones.add(tel);
                    adapter.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    private void setEmails(ArrayAdapter <Email> emails, ArrayAdapter adapter, long id) {
        String where = "_id = '" + id + "'";
        String col[] = {"_id", "endereco"};
        Cursor cursor = getRecords(Email.TABELA, col, where, null, "endereco COLLATE NOCASE ASC");
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    Email mail = new Email();
                    mail.set_id(cursor.getLong(0));
                    mail.setEndereco(cursor.getString(1));
                    emails.add(mail);
                    adapter.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    private boolean setEnderecos(ArrayAdapter <Endereco> enderecos, ArrayAdapter adapter, long id) {
        String where = "_id = '" + id + "'";
        String col[] = {"_id", "logradouro", "numero", "complemento", "bairro", "cidade", "uf", "cep"};
        Cursor cursor = getRecords(Endereco.TABELA, col, where, null, "logradouro COLLATE NOCASE ASC, numero COLLATE NOCASE ASC, complemento COLLATE NOCASE ASC");   // BUG: ORDER BY
        if (cursor == null) {
            return false;
        }
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Endereco endereco = getEndereco(cursor, true);
                enderecos.add(endereco);
                adapter.add(Utility.enderecoReduce(endereco));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return true;
    }

    private Servico getServico(Cursor cursor, boolean flag) {
        Servico servico = new Servico();
        if (flag == true) {
            servico.set_id(cursor.getLong(cursor.getColumnIndex(Servico._ID)));
        }
        servico.set_tipo(cursor.getLong(cursor.getColumnIndex(Servico._TIPO)));
        servico.setDescricao(cursor.getString(cursor.getColumnIndex(Servico.DESCRICAO)));
        servico.setValor(cursor.getDouble(cursor.getColumnIndex(Servico.VALOR)));
        return servico;
    }

    private void setArrayTelefone(String[] array, long id_telefone, int i) {
        String where = "_id = '" + id_telefone + "'";
        String col[] = {"_id", "numero"};
        Cursor cursor = getRecords(Telefone.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                array[i] = cursor.getString(1);
            }
            cursor.close();
        }
    }

    private void setArrayEmail(String[] array, long id_email, int i) {
        String where = "_id = '" + id_email + "'";
        String col[] = {"_id", "endereco"};
        Cursor cursor = getRecords(Email.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                array[i] = cursor.getString(1);
            }
            cursor.close();
        }
    }

    public static Domain getInstance() {
        if (instance == null) {
            instance = new Domain();
        }
        return instance;
    }

    public void setConnection(SQLiteDatabase connection) {
        this.connection = connection;
    }

    public String getError() {
        return error;
    }

    public int checkRazaoSocialForCliente(String razao_social) {
        String where = "razao_social = '" + razao_social + "'";
        String id[] = {"_id"};
        return getRecordFound(Cliente.TABELA, id, where);
    }

    public int checkCnpjForCliente(String cnpj) {
        String where = "cnpj = '" + cnpj + "'";
        String id[] = {"_id"};
        return getRecordFound(Cliente.TABELA, id, where);
    }

    public int checkUniqueKeyForServico(String descricao) {
        String where = "descricao = '" + descricao + "'";
        String id[] = {"_id"};
        return getRecordFound(Servico.TABELA, id, where);
    }

    public int checkUniqueKeyForTelefone(String numero) {
        String where = "numero = '" + numero + "'";
        String id[] = {"_id"};
        return getRecordFound(Telefone.TABELA, id, where);
    }

    public int checkUniqueKeyForEmail(String endereco) {
        String where = "endereco = '" + endereco + "'";
        String id[] = {"_id"};
        return getRecordFound(Email.TABELA, id, where);
    }

    public int checkUniqueKeyForContratoEvento(long id_contrato) {
        String where = "_contrato = '" + id_contrato + "'";
        String id[] = {"_id"};
        return getRecordFound(Evento.TABELA, id, where);
    }

    public int checkUniqueKeyForDescricaoEvento(String descricao) {
        String where = "descricao = '" + descricao + "'";
        String id[] = {"_id"};
        return getRecordFound(Evento.TABELA, id, where);
    }

    public int checkAllFieldsForEndereco(Endereco endereco) {
        String where = "logradouro = '" + endereco.getLogradouro() + "' AND ";
        where += "numero = '" + endereco.getNumero() + "' AND ";
        where += "complemento = '" + endereco.getComplemento() + "' AND ";
        where += "bairro = '" + endereco.getBairro() + "' AND ";
        where += "cidade = '" + endereco.getCidade() + "' AND ";
        where += "uf = '" + endereco.getUf() + "' AND ";
        where += "cep = '" + endereco.getCep() + "'";
        String _id[] = {"_id"};
        long result = getRecordId(Endereco.TABELA, _id, where);
        if (result > 0 && endereco.get_id() == result) {
            return 0;
        } else {
            return (int) result;
        }
    }

    public int checkIfContratoExists(Contrato contrato) {
        String t1 = Dates.getDefaultFormatFromSQLDateTime(contrato.getData_inicial());
        String t2 = Dates.getDefaultFormatFromSQLDateTime(contrato.getData_final());
        String where = "(('" + t1 + "' >= data_inicial AND '" + t1 + "' <= data_final) OR ('" + t2 + "' <= data_final AND '" + t2 + "' >= data_inicial) OR ('" + t1 + "' <= data_inicial AND '" + t2 + "' >= data_final))";
        String _id[] = {"_id"};
        long result = getRecordId(Contrato.TABELA, _id, where);
        if (result > 0 && contrato.get_id() == result) {
            return 0;
        } else {
            return (int) result;
        }
    }

    public long getIdTipo(Activity activity, String text) {
        String where = "descricao = '" + text + "'";
        String col[] = {"_id"};
        long result = 0;
        Cursor cursor = getRecords(Tipo.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = cursor.getLong(0);
            }
            cursor.close();
        }
        return result;
    }

    public String getTextTipo(Activity activity, long id) {
        String where = "_id = '" + id + "'";
        String col[] = {"descricao"};
        String result = "";
        Cursor cursor = getRecords(Tipo.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = cursor.getString(0);
            }
            cursor.close();
        }
        return result;
    }

    public long getIdCliente(Activity activity, String text) {
        String where = "razao_social = '" + text + "'";
        String col[] = {"_id"};
        long result = 0;
        Cursor cursor = getRecords(Cliente.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = cursor.getLong(0);
            }
            cursor.close();
        }
        return result;
    }

    public String getTextCliente(Activity activity, long id) {
        String where = "_id = '" + id + "'";
        String col[] = {"razao_social"};
        String result = "";
        Cursor cursor = getRecords(Cliente.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = cursor.getString(0);
            }
            cursor.close();
        }
        return result;
    }

    public long getIdServico(Activity activity, String text) {
        String where = "descricao = '" + text + "'";
        String col[] = {"_id"};
        long result = 0;
        Cursor cursor = getRecords(Servico.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = cursor.getLong(0);
            }
            cursor.close();
        }
        return result;
    }

    public long addCliente(Cliente cliente) {
        ContentValues values = putCliente(cliente);
        return insertRecord(Cliente.TABELA, values);
    }

    public long addEvento(Evento evento) {
        ContentValues values = putEvento(evento);
        return insertRecord(Evento.TABELA, values);
    }

    public long addTelefone(Telefone telefone) {
        ContentValues values = putTelefone(telefone);
        return insertRecord(Telefone.TABELA, values);
    }

    public long addEmail(Email email) {
        ContentValues values = putEmail(email);
        return insertRecord(Email.TABELA, values);
    }

    public long addEndereco(Endereco endereco) {
        ContentValues values = putEndereco(endereco);
        return insertRecord(Endereco.TABELA, values);
    }

    public long addContrato(Contrato contrato) {
        ContentValues values = putContrato(contrato);
        return insertRecord(Contrato.TABELA, values);
    }

    public long addServico(Servico servico) {
        ContentValues values = putServico(servico);
        return insertRecord(Servico.TABELA, values);
    }

    public long addPagamento(Pagamento pagamento) {
        ContentValues values = putPagamento(pagamento);
        return insertRecord(Pagamento.TABELA, values);
    }

    public long addTipoServico(Tipo tipo) {
        ContentValues values = putTipoServico(tipo);
        return insertRecord(Tipo.TABELA, values);
    }

    public int deleteTelefone(long id) {
        return deleteRecord(Telefone.TABELA, id);
    }

    public int deleteEmail(long id) {
        return deleteRecord(Email.TABELA, id);
    }

    public int deleteEndereco(long id) {
        return deleteRecord(Endereco.TABELA, id);
    }

    public int deleteContrato(long id, boolean flag) {
        if (flag == false) {
            return deleteRecord(Contrato.TABELA, id);
        }
        return deleteRecord(Contrato.TABELA, id, "_cliente");
    }

    public int deleteCliente(long id) {
        return deleteRecord(Cliente.TABELA, id);
    }

    public int deleteServico(long id) {
        return deleteRecord(Servico.TABELA, id);
    }

    public int deleteTipo(long id) {
        return deleteRecord(Tipo.TABELA, id);
    }

    public int deletePagamento(long id) {
        return deleteRecord(Pagamento.TABELA, id);
    }

    public int deleteEvento(long id) {
        return deleteRecord(Evento.TABELA, id);
    }

    public int deleteEventoServico(long evento_id) {
        return deleteRecord(EventoXServico.TABELA, evento_id, "_evento");
    }

    public int deleteEventoPagamento(long evento_id) {
        return deleteRecord(Pagamento.TABELA, evento_id, "_evento");
    }

    public long addEventoXServico(EventoXServico evento_x_xervico) {
        ContentValues values = putEventoXServico(evento_x_xervico);
        return insertRecord(EventoXServico.TABELA, values);
    }

    public long addClienteXTelefone(ClienteXTelefone cliente_x_telefone) {
        ContentValues values = putClienteXTelefone(cliente_x_telefone);
        return insertRecord(ClienteXTelefone.TABELA, values);
    }

    public long addClienteXEmail(ClienteXEmail cliente_x_email) {
        ContentValues values = putClienteXEmail(cliente_x_email);
        return insertRecord(ClienteXEmail.TABELA, values);
    }

    public long addClienteXEndereco(ClienteXEndereco cliente_x_endereco) {
        ContentValues values = putClienteXEndereco(cliente_x_endereco);
        return insertRecord(ClienteXEndereco.TABELA, values);
    }

    public int updateCliente(Cliente cliente) {
        ContentValues values = putCliente(cliente);
        return updateRecord(Cliente.TABELA, values, cliente.get_id());
    }

    public int updateServico(Servico servico) {
        ContentValues values = putServico(servico);
        return updateRecord(Servico.TABELA, values, servico.get_id());
    }

    public int updateEndereco(Endereco endereco) {
        ContentValues values = putEndereco(endereco);
        return updateRecord(Endereco.TABELA, values, endereco.get_id());
    }

    public int updateContrato(Contrato contrato) {
        ContentValues values = putContrato(contrato);
        return updateRecord(Contrato.TABELA, values, contrato.get_id());
    }

    public int updateTipoServico(Tipo tipo) {
        ContentValues values = putTipoServico(tipo);
        return updateRecord(Tipo.TABELA, values, tipo.get_id());
    }

    public int updateEvento(Evento evento) {
        ContentValues values = putEvento(evento);
        return updateRecord(Evento.TABELA, values, evento.get_id());
    }

    public int updatePagamento(Pagamento pagamento) {
        ContentValues values = putPagamento(pagamento);
        return updateRecord(Pagamento.TABELA, values, pagamento.get_id());
    }

    public int deleteClienteXTelefone(long id, boolean key) {
        String s;
        if (!key) {
            s = "_cliente";
        } else {
            s = "_telefone";
        }
        return deleteRecord(ClienteXTelefone.TABELA, id, s);
    }

    public int deleteClienteXEmail(long id, boolean key) {
        String s;
        if (!key) {
            s = "_cliente";
        } else {
            s = "_email";
        }
        return deleteRecord(ClienteXEmail.TABELA, id, s);
    }

    public int deleteClienteXEndereco(long id, boolean key) {
        String s;
        if (!key) {
            s = "_cliente";
        } else {
            s = "_endereco";
        }
        return deleteRecord(ClienteXEndereco.TABELA, id, s);
    }

    public int deleteEventoXServico(long id_evento, long id_servico) {
        return deleteRecord(EventoXServico.TABELA, id_evento, id_servico, "_evento", "_servico");
    }

    public Endereco selectEndereco(long id) {
        String where = "_id = '" + id + "'";
        String col[] = {"logradouro", "numero", "complemento", "bairro", "cidade", "uf", "cep"};
        Cursor cursor = getRecords(Endereco.TABELA, col, where, null, null);
        Endereco endereco = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                endereco = getEndereco(cursor, false);
            }
            cursor.close();
        }
        return endereco;
    }

    public Contrato selectContrato(long id) {
        String where = "_id = '" + id + "'";
        String col[] = {"_cliente", "data_inicial", "data_final"};
        Cursor cursor = getRecords(Contrato.TABELA, col, where, null, null);
        Contrato contrato = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                contrato = getContrato(cursor, false);
            }
            cursor.close();
        }
        return contrato;
    }

    public Servico selectServico(long id) {
        String where = "_id = '" + id + "'";
        String col[] = {"_tipo", "descricao", "valor"};
        Cursor cursor = getRecords(Servico.TABELA, col, where, null, null);
        Servico servico = null;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                servico = getServico(cursor, false);
            }
            cursor.close();
        }
        return servico;
    }

    public ArrayAdapter <Endereco> selectEnderecos(Activity activity, long id) {
        ArrayAdapter <Endereco> enderecos = new ArrayAdapter <Endereco> (activity, 0);
        String table = "", where1, col1[] = {"", ""};
        col1[0] = "_cliente";
        table = ClienteXEndereco.TABELA;
        where1 = col1[0] + " = '" + id + "'";
        col1[1] = "_endereco";
        Cursor cursor1 = getRecords(table, col1, where1, null, null);
        if (cursor1 != null) {
            if (cursor1.getCount() > 0) {
                cursor1.moveToFirst();
                do {
                    long id2 = cursor1.getLong(1);
                    String where2 = "_id = '" + id2 + "'";
                    String col2[] = {"_id", "logradouro", "numero", "complemento", "bairro", "cidade", "uf", "cep"};
                    Cursor cursor2 = getRecords(Endereco.TABELA, col2, where2, null, null);
                    if (cursor2 != null) {
                        if (cursor2.getCount() > 0) {
                            cursor2.moveToFirst();
                            do {
                                Endereco endereco = getEndereco(cursor2, true);
                                enderecos.add(endereco);
                            } while (cursor2.moveToNext());
                        }
                        cursor2.close();
                    }
                } while (cursor1.moveToNext());
            }
            cursor1.close();
        }
        if (enderecos.getCount() > 0)
            return enderecos;
        return null;
    }

    public ArrayAdapter <Contrato> selectContratos(Activity activity, long cliente_id) {
        ArrayAdapter <Contrato> contratos = new ArrayAdapter <Contrato> (activity, 0);
        String table = Contrato.TABELA, col[] = {"_id", "_cliente", "data_inicial", "data_final"};
        String where = col[1] + " = '" + cliente_id + "'";
        Cursor cursor = getRecords(table, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    Contrato contrato = getContrato(cursor, true);
                    contratos.add(contrato);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        if (contratos.getCount() > 0)
            return contratos;
        return null;
    }

    public ArrayAdapter<Cliente> findClientes(Context context, boolean flag) {
        ArrayAdapter<Cliente> adapter;
        if (flag) {
            adapter = new ClienteAdapter(context, R.layout.list_clientes);
        } else {
            adapter = new EventoClienteAdapter(context, R.layout.list_evento_clientes);
        }
        Cursor cursor = connection.query(Cliente.TABELA, null, null, null, null, null, "razao_social COLLATE NOCASE ASC");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Cliente cliente = new Cliente();
                cliente.set_id(cursor.getLong(cursor.getColumnIndex(Cliente._ID)));
                cliente.setRazao_social(cursor.getString(cursor.getColumnIndex(Cliente.RAZAO_SOCIAL)));
                cliente.setCnpj(cursor.getString(cursor.getColumnIndex(Cliente.CNPJ)));
                adapter.add(cliente);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return adapter;
    }

    public ContratoAdapter findContratos(Context context, long cliente_id) {
        ContratoAdapter adapter = new ContratoAdapter(context, R.layout.list_evento_contratos);
        String col[] = {"_id", "_cliente", "data_inicial", "data_final"};
        String where;
        if (cliente_id == 0) {
            where = "1";
        } else {
            where = col[1] + " = '" + cliente_id + "'";
        }
        Cursor cursor = getRecords(Contrato.TABELA, col, where, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Contrato contrato = new Contrato();
                contrato.set_id(cursor.getLong(cursor.getColumnIndex(Contrato._ID)));
                contrato.set_cliente(cursor.getLong(cursor.getColumnIndex(Contrato._CLIENTE)));
                contrato.setData_inicial(Dates.getSQLDateTime(cursor.getString(cursor.getColumnIndex(Contrato.DATA_INICIAL))));
                contrato.setData_final(Dates.getSQLDateTime(cursor.getString(cursor.getColumnIndex(Contrato.DATA_FINAL))));
                adapter.add(contrato);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return adapter;
    }

    public ServicoAdapter findServicos(Context context, Database database) {
        ServicoAdapter adapter = new ServicoAdapter(context, R.layout.list_servicos, this, database);
        Cursor cursor = connection.query(Servico.TABELA, null, null, null, null, null, "descricao COLLATE NOCASE ASC");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Servico servico = getServico(cursor, true);
                adapter.add(servico);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return adapter;
    }

    public EventoAdapter findEventos(Context context) {
        EventoAdapter adapter = new EventoAdapter(context, R.layout.list_eventos);
        Cursor cursor = connection.query(Evento.TABELA, null, null, null, null, null, "descricao COLLATE NOCASE ASC");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Evento evento = new Evento();
                evento.set_id(cursor.getLong(cursor.getColumnIndex(Evento._ID)));
                evento.set_contrato(cursor.getLong(cursor.getColumnIndex(Evento._CONTRATO)));
                evento.setDescricao(cursor.getString(cursor.getColumnIndex(Evento.DESCRICAO)));
                adapter.add(evento);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return adapter;
    }

    public EventoServicoAdapter findServicos(Context context, long id_tipo) {
        EventoServicoAdapter adapter = new EventoServicoAdapter(context, R.layout.list_evento_servicos);
        String col[] = {"_id", "_tipo", "descricao", "valor"};
        String where = col[1] + " = '" + id_tipo + "'";
        Cursor cursor = getRecords(Servico.TABELA, col, where, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Servico servico = getServico(cursor, true);
                adapter.add(servico);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return adapter;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public TipoAdapter findTipos(Context context) {
        TipoAdapter adapter = new TipoAdapter(context, R.layout.list_tipos);
        Cursor cursor = connection.query(Tipo.TABELA, null, null, null, null, null, "descricao COLLATE NOCASE ASC");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Tipo tipo = new Tipo();
                tipo.set_id(cursor.getLong(cursor.getColumnIndex(Tipo._ID)));
                tipo.setDescricao(cursor.getString(cursor.getColumnIndex(Tipo.DESCRICAO)));
                adapter.add(tipo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return adapter;
    }

    public void selectAllTipos(ArrayAdapter<String> adapter) {
        Cursor cursor = connection.query(Tipo.TABELA, null, null, null, null, null, "descricao COLLATE NOCASE ASC");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                adapter.add(cursor.getString(cursor.getColumnIndex(Tipo.DESCRICAO)));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public double getTotalServicos(ArrayAdapter<String> ad_servicos) {
        double result = 0.00;
        int count = ad_servicos.getCount();
        String[] str = new String[count];
        for (int i = 0; i < count; i++) {
            str[i] = ad_servicos.getItem(i);
        }
        Cursor cursor = connection.query(Servico.TABELA, null, "descricao IN (" + makePlaceholders(str.length) + ")", str, null, null, "descricao COLLATE NOCASE ASC");
        if (cursor != null) {
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    result += cursor.getDouble(cursor.getColumnIndex(Servico.VALOR));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return result;
    }

    public boolean setEventoServicos(ArrayAdapter<Servico> servicos, ArrayAdapter <EventoXServico> evento_x_servico, ArrayAdapter adapter, long id) {
        String where = "_evento = '" + id + "'";
        String col[] = {"_evento", "_servico"};
        Cursor cursor = getRecords(EventoXServico.TABELA, col, where, null, null);
        boolean flag = false;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    EventoXServico e_x_s = new EventoXServico();
                    long id_serv = cursor.getLong(1);
                    e_x_s.set_evento(cursor.getLong(0));
                    e_x_s.set_servico(id_serv);
                    evento_x_servico.add(e_x_s);
                    setServicos(servicos, adapter, id_serv);
                } while (cursor.moveToNext());
                flag = true;
            }
            cursor.close();
        }
        return flag;
    }

    public boolean setEventoPagamento(long evento_id, Pagamento pagamento) {
        String where = "_evento = '" + evento_id + "'";
        String col[] = {"_id", "_evento", "total", "desconto"};
        Cursor cursor = getRecords(Pagamento.TABELA, col, where, null, null);
        boolean flag = false;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                pagamento.set_id(cursor.getLong(0));
                pagamento.set_evento(cursor.getLong(1));
                pagamento.setTotal(cursor.getDouble(2));
                pagamento.setDesconto(cursor.getDouble(3));
                flag = true;
            }
            cursor.close();
        }
        return flag;
    }

    public boolean setClienteTelefones(ArrayAdapter<Telefone> telefones, ArrayAdapter <ClienteXTelefone> cliente_x_telefone, ArrayAdapter adapter, long id) {
        String where = "_cliente = '" + id + "'";
        String col[] = {"_cliente", "_telefone"};
        Cursor cursor = getRecords(ClienteXTelefone.TABELA, col, where, null, null);
        boolean flag = false;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    long id2 = cursor.getLong(1);
                    ClienteXTelefone c_x_t = new ClienteXTelefone();
                    c_x_t.set_cliente(cursor.getLong(0));
                    c_x_t.set_telefone(id2);
                    cliente_x_telefone.add(c_x_t);
                    setTelefones(telefones, adapter, id2);
                } while (cursor.moveToNext());
                flag = true;
            }
            cursor.close();
        }
        return flag;
    }

    public boolean setClienteEmails(ArrayAdapter <Email> emails, ArrayAdapter <ClienteXEmail> cliente_x_email, ArrayAdapter adapter, long id) {
        String where = "_cliente = '" + id + "'";
        String col[] = {"_cliente", "_email"};
        Cursor cursor = getRecords(ClienteXEmail.TABELA, col, where, null, null);
        boolean flag = false;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    long id2 = cursor.getLong(1);
                    ClienteXEmail c_x_e = new ClienteXEmail();
                    c_x_e.set_cliente(cursor.getLong(0));
                    c_x_e.set_email(id2);
                    cliente_x_email.add(c_x_e);
                    setEmails(emails, adapter, id2);
                } while (cursor.moveToNext());
                flag = true;
            }
            cursor.close();
        }
        return flag;
    }

    public boolean setClienteEnderecos(ArrayAdapter <Endereco> enderecos, ArrayAdapter <ClienteXEndereco> cliente_x_endereco, ArrayAdapter adapter, long id) {
        String where = "_cliente = '" + id + "'";
        String col[] = {"_cliente", "_endereco"};
        Cursor cursor = getRecords(ClienteXEndereco.TABELA, col, where, null, null);
        boolean flag = false;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    long id2 = cursor.getLong(1);
                    ClienteXEndereco c_x_e = new ClienteXEndereco();
                    c_x_e.set_cliente(cursor.getLong(0));
                    c_x_e.set_endereco(id2);
                    cliente_x_endereco.add(c_x_e);
                    if (setEnderecos(enderecos, adapter, id2) == false) {
                        cursor.close();
                        return false;
                    }
                } while (cursor.moveToNext());
                flag = true;
            }
            cursor.close();
        }
        return flag;
    }

    public boolean setClienteContratos(ArrayAdapter <Contrato> contratos, ArrayAdapter adapter, long id) {
        String where = "_cliente = '" + id + "'";
        String col[] = {"_id", "_cliente", "data_inicial", "data_final"};
        Cursor cursor = getRecords(Contrato.TABELA, col, where, null, null);
        boolean flag = false;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    Contrato ct = new Contrato();
                    ct.set_id(cursor.getLong(0));
                    ct.set_cliente(cursor.getLong(1));
                    ct.setData_inicial(Dates.getSQLDateTime(cursor.getString(2)));
                    ct.setData_final(Dates.getSQLDateTime(cursor.getString(3)));
                    contratos.add(ct);
                    String text = Utility.contratoReduce(ct);
                    adapter.add(text);
                } while (cursor.moveToNext());
                flag = true;
            }
            cursor.close();
        }
        return flag;
    }

    public int getCountClienteXTelefones(long id) {
        int result = 0;
        String where = "_cliente = '" + id + "'";
        String col[] = {"_cliente", "_telefone"};
        Cursor cursor = getRecords(ClienteXTelefone.TABELA, col, where, null, null);
        if (cursor != null) {
            result = cursor.getCount();
            cursor.close();
        }
        return result;
    }

    public int getCountClienteXEmails(long id) {
        int result = 0;
        String where = "_cliente = '" + id + "'";
        String col[] = {"_cliente", "_email"};
        Cursor cursor = getRecords(ClienteXEmail.TABELA, col, where, null, null);
        if (cursor != null) {
            result = cursor.getCount();
            cursor.close();
        }
        return result;
    }

    public void setArrayClienteTelefones(String[] array, long id_medico) {
        String where = "_cliente = '" + id_medico + "'";
        String col[] = {"_cliente", "_telefone"};
        Cursor cursor = getRecords(ClienteXTelefone.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int i = 0;
                do {
                    long id_telefone = cursor.getLong(1);
                    setArrayTelefone(array, id_telefone, i);
                    i++;
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    public void setArrayClienteEmails(String[] array, long id_medico) {
        String where = "_cliente = '" + id_medico + "'";
        String col[] = {"_cliente", "_email"};
        Cursor cursor = getRecords(ClienteXEmail.TABELA, col, where, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int i = 0;
                do {
                    long id_email = cursor.getLong(1);
                    setArrayEmail(array, id_email, i);
                    i++;
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }
}
