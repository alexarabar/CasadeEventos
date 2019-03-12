package br.com.alexandrebarboza.casadeeventos.Utility.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.alexandrebarboza.casadeeventos.R;
import br.com.alexandrebarboza.casadeeventos.Domain.Entity.Servico;

/**
 * Created by Alexandre on 23/04/2017.
 */

public class EventoServicoAdapter extends ArrayAdapter<Servico> implements View.OnClickListener {
    private int resource;
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<String> array_list;

    private boolean compareToList(String text) {
        if (array_list == null) {
            return false;
        }
        for (int i = 0; i < array_list.size(); i++) {
            if (array_list.get(i).equals(text)) {
                return true;
            }
        }
        return false;
    }

    private int getPositionOfList(String text) {
        for (int i = 0; i < array_list.size(); i++) {
            if (array_list.get(i).equals(text)) {
                return i;
            }
        }
        return -1;
    }
    public ArrayList<String> getArrayList() {
        return array_list;
    }
    public EventoServicoAdapter(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context  = context;
        this.resource = resource;
        Activity activity = (Activity) context;
        Intent intent = activity.getIntent();
        this.array_list = intent.getStringArrayListExtra("LIST_SERVICOS");
        setNotifyOnChange (true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        EventoServicoAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new EventoServicoAdapter.ViewHolder();
            view = inflater.inflate(resource, parent, false);
            holder.text_evento_servico = (TextView) view.findViewById(R.id.text_evento_servico);
            view.setTag(holder);
        } else {
            holder = (EventoServicoAdapter.ViewHolder) convertView.getTag();
            view = convertView;
        }
        Servico servico = getItem(position);
        holder.text_evento_servico.setText(servico.getDescricao());
        holder.check_evento_servico = (CheckBox) view.findViewById(R.id.check_evento_servico);
        holder.check_evento_servico.setOnClickListener(this);
        holder.check_evento_servico.setTag(holder.text_evento_servico);
        holder.text_evento_servico.setTag(holder.check_evento_servico);
        holder.check_evento_servico.setChecked(compareToList(holder.text_evento_servico.getText().toString()));
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_evento_servico:
                CheckBox check = (CheckBox) v;
                TextView text = (TextView) check.getTag();
                if (check.isChecked()) {
                    array_list.add(text.getText().toString());
                } else {
                    array_list.remove(getPositionOfList(text.getText().toString()));
                }
                break;
        }
    }

    static class ViewHolder {
        TextView text_evento_servico;
        CheckBox check_evento_servico;
    }
}
