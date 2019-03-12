package br.com.alexandrebarboza.casadeeventos.Utility.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Cliente;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Contrato;
import br.com.alexandrebarboza.casadeeventos.R;

/**
 * Created by Alexandre on 22/04/2017.
 */

public class ContratoAdapter extends ArrayAdapter<Contrato> {
    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;

    public ContratoAdapter(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context  = context;
        this.resource = resource;
        setNotifyOnChange (true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ContratoAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new ContratoAdapter.ViewHolder();
            view = inflater.inflate(resource, parent, false);
            holder.text_contrato = (TextView) view.findViewById(R.id.text_contrato);
            view.setTag(holder);
        } else {
            holder = (ContratoAdapter.ViewHolder) convertView.getTag();
            view = convertView;
        }
        Contrato contrato = getItem(position);
        holder.text_contrato.setText(contrato.toString());
        return view;
    }

    static class ViewHolder {
        TextView text_contrato;
    }
}
