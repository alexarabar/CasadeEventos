package br.com.alexandrebarboza.casadeeventos.Database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.alexandrebarboza.casadeeventos.Database.Script.*;

/**
 * Created by Alexandre on 10/04/2017.
 */

public class Database extends SQLiteOpenHelper {
    private static final String NOME_BANCO =  "Evento";
    private static final int VERSAO =  10;
    private static Database instance = null;
    private SQLiteDatabase connection;
    private String error;

    private Database(Context context) { // Singleton
        super(context, NOME_BANCO, null, VERSAO);
        connection = null;
        error = "";
    }

    public static Database getInstance(Context context) {
        if (instance == null) {
            instance = new Database(context);
        }
        return instance;
    }

    public SQLiteDatabase getConnection() {
        return connection;
    }

    public String getError(){
        return error;
    }

    public boolean setReadable() {
        try {
            connection = getReadableDatabase();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
            return false;
        }
    }

    public boolean setWritable() {
        try {
            connection = getWritableDatabase();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
            return false;
        }
    }

    public void Close() {
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(TableCreate.Telefones());
            db.execSQL(TableCreate.Emails());
            db.execSQL(TableCreate.Enderecos());
            db.execSQL(TableCreate.Clientes());
            db.execSQL(TableCreate.Contratos());
            db.execSQL(TableCreate.Tipos());
            db.execSQL(TableCreate.Servicos());
            db.execSQL(TableCreate.Eventos());
            db.execSQL(TableCreate.Pagamentos());
            db.execSQL(TableCreate.ClienteXTelefone());
            db.execSQL(TableCreate.ClienteXEmail());
            db.execSQL(TableCreate.ClienteXEndereco());
            db.execSQL(TableCreate.EventoXServico());
            db.execSQL(IndexCreate.uk_endereco());
            db.execSQL(IndexCreate.uk_cliente_x_telefone());
            db.execSQL(IndexCreate.uk_cliente_x_email());
            db.execSQL(IndexCreate.uk_cliente_x_endereco());
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(IndexDrop.uk_cliente_x_telefone());
            db.execSQL(IndexDrop.uk_cliente_x_email());
            db.execSQL(IndexDrop.uk_cliente_x_endereco());
            db.execSQL(IndexDrop.uk_endereco());
            db.execSQL(TableDrop.ClienteXTelefone());
            db.execSQL(TableDrop.ClienteXEmail());
            db.execSQL(TableDrop.ClienteXEndereco());
            db.execSQL(TableDrop.EventoXServico());
            db.execSQL(TableDrop.Pagamentos());
            db.execSQL(TableDrop.Eventos());
            db.execSQL(TableDrop.Servicos());
            db.execSQL(TableDrop.Tipos());
            db.execSQL(TableDrop.Contratos());
            db.execSQL(TableDrop.Clientes());
            db.execSQL(TableDrop.Telefones());
            db.execSQL(TableDrop.Emails());
            db.execSQL(TableDrop.Enderecos());
            onCreate(db);
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
        }
    }
}
