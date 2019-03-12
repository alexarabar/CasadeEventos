package br.com.alexandrebarboza.casadeeventos.Utility.Messages;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import br.com.alexandrebarboza.casadeeventos.Domain.Domain;
import br.com.alexandrebarboza.casadeeventos.Database.Database;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Endereco;
import br.com.alexandrebarboza.casadeeventos.Utility.Utility;
import br.com.alexandrebarboza.casadeeventos.R;

/**
 * Created by Alexandre on 27/04/2017.
 */

public class ProgressClientes extends AsyncTask<String, Integer, String> {
    private Database database;
    private Domain domain;
    private Activity activity;
    private ProgressDialog status;
    private ListView list;

    public ProgressClientes(Database database, Domain domain, Activity activity, ProgressDialog status, ListView list) {
        this.database = database;
        this.domain = domain;
        this.activity = activity;
        this.status = status;
        this.list = list;
        list.getCount();
    }

    @Override
    protected void onPreExecute() {
        status.show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        status.setProgress(progress[0]);
        super.onProgressUpdate(progress);
    }

    @Override
    protected String doInBackground(String... params) {
        for (int i = 0; i < list.getCount(); i++) {
            if (isCancelled()) {
                break;
            }
            String nome = list.getItemAtPosition(i).toString();
            String[] tels = null;
            String[] mails = null;
            long id = domain.getIdCliente(activity, nome);
            int c1 = domain.getCountClienteXTelefones(id);
            int c2 = domain.getCountClienteXEmails(id);
            if (c1 > 0) {
                tels = new String[c1];
            }
            if (c2 > 0) {
                mails = new String[c2];
            }
            domain.setArrayClienteTelefones(tels, id);
            domain.setArrayClienteEmails(mails, id);
            ArrayAdapter<Endereco> enderecos = domain.selectEnderecos(activity, id);
            final String res = Utility.exportContacts(activity, activity.getResources().getString(R.string.str_clientes), nome, tels, mails, enderecos);
            final int val = i + 1;
            if (res != null) {
                publishProgress(val);
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        status.setMessage(res + " (" + val + ")");
                    }
                });
            } else {
                break;
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        status.dismiss();
        database.Close();
    }

    @Override
    protected void onCancelled() {
        status.cancel();
        database.Close();
    }
}
