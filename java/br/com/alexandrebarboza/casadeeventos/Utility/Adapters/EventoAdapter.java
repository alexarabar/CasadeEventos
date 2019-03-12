package br.com.alexandrebarboza.casadeeventos.Utility.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.alexandrebarboza.casadeeventos.R;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Evento;
import br.com.alexandrebarboza.casadeeventos.Utility.Glossary;

/**
 * Created by Alexandre on 24/04/2017.
 */

public class EventoAdapter extends ArrayAdapter<Evento> {
    private int resource = 0;
    private LayoutInflater inflater;
    private Context context;

    public EventoAdapter(Context context, int resource) {
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
            holder.text_cor_evento = (TextView) view.findViewById(R.id.text_cor_evento);
            holder.text_descricao_evento = (TextView) view.findViewById(R.id.text_descricao_evento);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            view = convertView;
        }
        Evento evento =  getItem(position);
        Evento previous = null;
        String compare   = " ";
        if (position > 0) {
            previous = getItem(position - 1);
            if (previous.getDescricao().length() > 0) {
                compare = previous.getDescricao().toUpperCase().substring(0, 1);
            }
        }
        Glossary.SetGlossary(holder.text_cor_evento, evento.getDescricao(), context, position, compare);
        holder.text_descricao_evento.setText(evento.getDescricao());
        return view;
    }

    static class ViewHolder {
        TextView text_cor_evento;
        TextView text_descricao_evento;
    }
}
