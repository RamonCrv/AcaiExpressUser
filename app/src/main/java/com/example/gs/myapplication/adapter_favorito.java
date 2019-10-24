package com.example.gs.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class adapter_favorito extends ArrayAdapter {
private  String[] nome;
private  String[] aberto;
private  String[] naosei1;
private  String[] naosei2;


private Activity context;

    public  adapter_favorito(Activity context,String[] nome,String[] aberto,String[] naosei1,String[] naosei2 ){
    super(context, R.layout.adapter_favorito,nome);
    this.context = context;
    this.nome = nome;
    this.aberto = aberto;
    this.naosei1 = naosei1;
    this.naosei2 = naosei2;
}

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View res = convertView;
        adapter_favorito.ViewHolder viewHolder = null;
        if (res == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            res = layoutInflater.inflate(R.layout.adapter_favorito,null,true);
            viewHolder = new adapter_favorito.ViewHolder(res);
            res.setTag(viewHolder);
        }else{
            viewHolder = (adapter_favorito.ViewHolder) res.getTag();
        }
        viewHolder.nome.setText(nome[position]);
        viewHolder.aberto.setText(aberto[position]);
        viewHolder.naosei1.setText(naosei1[position]);
        viewHolder.naosei2.setText(naosei2[position]);
        return res;
    }

    static  class ViewHolder{
    TextView nome;
    TextView aberto;
    TextView naosei1;
    TextView naosei2;

    ViewHolder(View view){
        nome = view.findViewById(R.id.localname);
        aberto = view.findViewById(R.id.aberto);
        naosei1 = view.findViewById(R.id.textView4);
        naosei2 = view.findViewById(R.id.textView3);
    }

}



























}
