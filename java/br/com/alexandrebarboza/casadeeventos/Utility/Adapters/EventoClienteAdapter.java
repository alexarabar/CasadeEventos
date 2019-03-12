package br.com.alexandrebarboza.casadeeventos.Utility.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Cliente;
import br.com.alexandrebarboza.casadeeventos.R;

/**
 * Created by Alexandre on 21/04/2017.
 */

public class EventoClienteAdapter extends ArrayAdapter<Cliente> {
    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;

    public EventoClienteAdapter(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context  = context;
        this.resource = resource;
        setNotifyOnChange (true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        EventoClienteAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new EventoClienteAdapter.ViewHolder();
            view = inflater.inflate(resource, parent, false);
            holder.text_razao_social_cliente = (TextView) view.findViewById(R.id.text_razao_social_cliente2);
            view.setTag(holder);
        } else {
            holder = (EventoClienteAdapter.ViewHolder) convertView.getTag();
            view = convertView;
        }
        Cliente cliente = getItem(position);
        holder.text_razao_social_cliente.setText(cliente.getRazao_social());
        return view;
    }

    static class ViewHolder {
        TextView text_razao_social_cliente;
    }
}
