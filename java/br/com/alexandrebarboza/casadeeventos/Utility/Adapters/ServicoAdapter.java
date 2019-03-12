package br.com.alexandrebarboza.casadeeventos.Utility.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.alexandrebarboza.casadeeventos.Database.Database;
import br.com.alexandrebarboza.casadeeventos.Domain.Domain;
import br.com.alexandrebarboza.casadeeventos.R;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Servico;
import br.com.alexandrebarboza.casadeeventos.Utility.Connector;
import br.com.alexandrebarboza.casadeeventos.Utility.Glossary;

/**
 * Created by Alexandre on 17/04/2017.
 */

public class ServicoAdapter extends ArrayAdapter<Servico> {
    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;
    Database database;
    Domain domain;

    public ServicoAdapter(Context context, int resource, Domain domain, Database database) {
        super(context, resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context  = context;
        this.resource = resource;
        this.database = database;
        this.domain   = domain;
        setNotifyOnChange (true);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            view = inflater.inflate(resource, parent, false);
            holder.text_cor_servico = (TextView) view.findViewById(R.id.text_cor_servico);
            holder.text_tipo_servico = (TextView) view.findViewById(R.id.text_tipo_servico);
            holder.text_descricao_servico = (TextView) view.findViewById(R.id.text_descricao_servico);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            view = convertView;
        }
        Servico servico =  getItem(position);
        Servico previous = null;
        String compare   = " ";
        if (position > 0) {
            previous = getItem(position - 1);
            if (previous.getDescricao().length() > 0) {
                compare = previous.getDescricao().toUpperCase().substring(0, 1);
            }
        }
        Glossary.SetGlossary(holder.text_cor_servico, servico.getDescricao(), context, position, compare);

        Activity act = (Activity) context;
        long  i_tipo = servico.get_tipo();
        if (!Connector.OpenDatabase(act.getResources(), act, database, domain, false)) {
            return null;
        }
        String res =  domain.getTextTipo(act, i_tipo);
        holder.text_tipo_servico.setText(res);
        holder.text_descricao_servico.setText(servico.getDescricao());
        database.Close();
        return view;
    }

    static class ViewHolder {
        TextView text_cor_servico;
        TextView text_tipo_servico;
        TextView text_descricao_servico;
    }
}
