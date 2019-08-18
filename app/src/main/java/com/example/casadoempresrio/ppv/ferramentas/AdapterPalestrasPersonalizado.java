package com.example.casadoempresrio.ppv.ferramentas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.casadoempresrio.ppv.objetos.Palestra;
import com.example.casadoempresrio.R;

import java.util.List;

public class AdapterPalestrasPersonalizado extends BaseAdapter {

    private final List<Palestra> palestras;
    private final Activity act;

    public AdapterPalestrasPersonalizado(List<Palestra> p, Activity act) {
        this.palestras = p;
        this.act = act;
    }

    @Override
    public int getCount() {
        return palestras.size();
    }

    @Override
    public Object getItem(int position) {
        return palestras.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public String getItemIdString(int pos){
        return palestras.get(pos).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View view = act.getLayoutInflater().inflate(R.layout.lista_palestra_item, parent, false);

        Palestra p = palestras.get(position);

        TextView lista_item_titulo = view.findViewById(R.id.lista_palestra_titulo);
        TextView lista_item_descricao = view.findViewById(R.id.lista_palestra_descricao);
        ImageView lista_item_imagem = view.findViewById(R.id.lista_palestra_imagem);

        lista_item_titulo.setText(p.getTitulo());
        lista_item_descricao.setText(p.getDescricao());

        String categoria = p.getCategoria();

        switch (categoria){

            case "Tecnologia":
                lista_item_imagem.setImageResource(R.drawable.tec);
                break;

            case "Empreendedorismo":
                lista_item_imagem.setImageResource(R.drawable.empreendedorismo_icon);
                break;

            case "Saúde":
                lista_item_imagem.setImageResource(R.drawable.saude_icon);
                break;

            case "Lazer":
                lista_item_imagem.setImageResource(R.drawable.lazer_icon);
                break;

            case "Motivacional":
                lista_item_imagem.setImageResource(R.drawable.motivacional_icon2);
                break;

            default:
                lista_item_imagem.setImageResource(R.drawable.android_green);
                break;
        }

        return view;
    }
}
