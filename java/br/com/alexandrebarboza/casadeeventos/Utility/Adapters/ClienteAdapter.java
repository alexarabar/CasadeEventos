package br.com.alexandrebarboza.casadeeventos.Utility.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Cliente;
import br.com.alexandrebarboza.casadeeventos.R;
import br.com.alexandrebarboza.casadeeventos.Utility.Glossary;

/**
 * Created by Alexandre on 12/04/2017.
 */

public class ClienteAdapter extends ArrayAdapter<Cliente> {
    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;

    public ClienteAdapter(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context  = context;
        this.resource = resource;
        setNotifyOnChange (true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            view = inflater.inflate(resource, parent, false);
            holder.text_cor_cliente = (TextView) view.findViewById(R.id.text_cor_cliente);
            holder.text_razao_social_cliente = (TextView) view.findViewById(R.id.text_razao_social_cliente);
            holder.text_cnpj_cliente = (TextView) view.findViewById(R.id.text_cnpj_cliente);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            view = convertView;
        }
        Cliente cliente =  getItem(position);
        Cliente previous = null;
        String compare   = " ";
        if (position > 0) {
            previous = getItem(position - 1);
            if (previous.getRazao_social().length() > 0) {
                compare = previous.getRazao_social().toUpperCase().substring(0, 1);
            }
        }

        Glossary.SetGlossary(holder.text_cor_cliente, cliente.getRazao_social(), context, position, compare);

        holder.text_razao_social_cliente.setText(cliente.getRazao_social());
        holder.text_cnpj_cliente.setText(cliente.getCnpj());
        return view;
    }

    static class ViewHolder {
        TextView text_cor_cliente;
        TextView text_razao_social_cliente;
        TextView text_cnpj_cliente;
    }
}