package com.example.gs.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Lista extends AppCompatActivity {
   // private static final String TAG = "Lista";
    ArrayList<Person> arrayList;
    ListView listView;
    ArrayAdapter arrayAdapter;


   /* String[] nome = {"nome aki"};
    String[] aberto = {"aberto aki"};
    String[] naosei1 = {"naosei1 aki"};
    String[] naosei2 = {"naosei2 aki"};*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
       // Log.d(TAG, "onCreate: Started.");
        arrayList = new ArrayList<>();
        listView =(ListView) findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter(this, R.layout.adapter_favorito, R.id.localname,arrayList) {

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView localname = (TextView) view.findViewById(R.id.localname);
                TextView Aberto = (TextView) view.findViewById(R.id.aberto);
                TextView naosei1 = (TextView) view.findViewById(R.id.textView4);
                TextView naosei2 = (TextView) view.findViewById(R.id.textView3);

                localname.setText(arrayList.get(position).Name);
                Aberto.setText(arrayList.get(position).Sex);
                naosei1.setText(arrayList.get(position).Birthday);
                naosei2.setText(arrayList.get(position).Naosei0);
                return view;
            }
        };
        arrayList.add(new Person("GS1","gs","abretoo","yzy"));
        arrayList.add(new Person("GS2","gss","abretoo","yzy"));
        arrayList.add(new Person("GS3","glks","abretoo","yzy"));
        arrayList.add(new Person("GS4","gfs","abretoo","yzy"));
        arrayList.add(new Person("GS5","gis","abretoo","yzy"));
        arrayList.add(new Person("GS6","guys","abretoo","yzy"));
        arrayList.add(new Person("GS7","gsgf","abretoo","yzy"));
        arrayList.add(new Person("GS8","kjgs","abretoo","yzy"));
        arrayList.add(new Person("GS9","ghgs","abretoo","yzy"));
        arrayList.add(new Person("GS0","grhs","abretoo","yzy"));
        arrayList.add(new Person("GS11","ghgs","abretoo","yzy"));
        arrayList.add(new Person("GS22","gkls","abretoo","yzy"));
        arrayList.add(new Person("GS33","gwers","abretoo","yzy"));
        arrayList.add(new Person("GS44","gers","abretoo","yzy"));
        arrayList.add(new Person("GS55","gsbnm","abretoo","yzy"));
        arrayList.add(new Person("GS66","gdfs","abretoo","yzy"));
        arrayList.add(new Person("GS77","gskhj","abretoo","yzy"));
        arrayList.add(new Person("GS88","gskjl","abretoo","yzy"));
        arrayList.add(new Person("GS99","gsiyu","abretoo","yzy"));
        arrayList.add(new Person("GS00","gsçio","abretoo","yzy"));
        arrayList.add(new Person("GS21","gs657","abretoo","yzy"));
        arrayList.add(new Person("GS23","gs345","abretoo","yzy"));
        arrayList.add(new Person("GS34","gs678","abretoo","yzy"));
        arrayList.add(new Person("GS45","gs78","abretoo","yzy"));
        arrayList.add(new Person("GS42","gs2","abretoo","yzy"));
        arrayList.add(new Person("GS56","gs45","abretoo","yzy"));








        arrayAdapter.notifyDataSetChanged();
        listView.setAdapter((arrayAdapter));


      /* adapter_favorito adapter_favorito = new adapter_favorito(this,nome,aberto,naosei1,naosei2);
       listView.setAdapter(adapter_favorito);
*/



































    }
}
