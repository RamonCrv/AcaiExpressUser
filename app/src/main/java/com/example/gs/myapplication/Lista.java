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

                localname.setText(arrayList.get(position).Name);
                Aberto.setText(arrayList.get(position).Sex);
                naosei1.setText(arrayList.get(position).Birthday);


                return view;
            }
        };
        arrayList.add(new Person("GS","gs","abretoo"));
        arrayList.add(new Person("gg1","gs123","abretoo31"));
        arrayList.add(new Person("gg2","gs124","abretoo32"));
        arrayList.add(new Person("gg3","gs125","abretoo33"));
        arrayList.add(new Person("gg4","gs126","abretoo34"));
        arrayList.add(new Person("gg5","gs127","abretoo35"));
        arrayList.add(new Person("gg6","gs128","abretoo36"));
        arrayList.add(new Person("gg7","gs129","abretoo37"));
        arrayList.add(new Person("gg7","gs129","abretoo37"));
        arrayList.add(new Person("gg7","gs129","abretoo37"));
        arrayList.add(new Person("gg7","gs129","abretoo37"));
        arrayList.add(new Person("gg7","gs129","abretoo37"));
        arrayList.add(new Person("gg7","gs129","abretoo37"));
        arrayList.add(new Person("gg7","gs129","abretoo37"));
        arrayList.add(new Person("gg7","gs129","abretoo37"));
        arrayList.add(new Person("gg7","gs129","abretoo37"));
        arrayList.add(new Person("gg7","gs129","abretoo37"));
        arrayList.add(new Person("gg7","gs129","abretoo37"));
        arrayList.add(new Person("gg7","gs129","abretoo37"));
        arrayList.add(new Person("gg7","gs129","abretoo37"));
        arrayList.add(new Person("gg7","gs129","abretoo37"));
        arrayList.add(new Person("gg7","gs129","abretoo37"));





        arrayAdapter.notifyDataSetChanged();
        listView.setAdapter((arrayAdapter));


      /* adapter_favorito adapter_favorito = new adapter_favorito(this,nome,aberto,naosei1,naosei2);
       listView.setAdapter(adapter_favorito);
*/



































    }
}
