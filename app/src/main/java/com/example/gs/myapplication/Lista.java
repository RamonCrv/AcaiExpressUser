package com.example.gs.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firestore.v1.Value;

import java.util.ArrayList;
import java.util.Map;

public class Lista extends AppCompatActivity {
   // private static final String TAG = "Lista";
    ArrayList<Person> arrayList;
    ListView listView;
    ArrayAdapter arrayAdapter;
    FirebaseAuth auth;
    String idpT;


   /* String[] nome = {"nome aki"};
    String[] aberto = {"aberto aki"};
    String[] naosei1 = {"naosei1 aki"};
    String[] naosei2 = {"naosei2 aki"};*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        auth = FirebaseAuth.getInstance();
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

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference  pontoRef = mDatabase.child("Usuario").child(auth.getCurrentUser().getUid()).child("Favorito");


        pontoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                }else {
                    Toast.makeText(Lista.this, "Você não possui postos favoritados no momento2",Toast.LENGTH_SHORT).show();
                }
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Map<String, String> map = (Map) postSnapshot.getValue();
                    String teste = postSnapshot.getKey();
                    //idpT = map.values().toString();
                    //Toast.makeText(Lista.this, i+": "+teste,Toast.LENGTH_SHORT).show();


                    DatabaseReference mDatabase2;
                    mDatabase2 = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference  pontoRef2 = mDatabase2.child("Ponto").child(teste);
                    pontoRef2.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String nomeDoPt = dataSnapshot.child("nome").getValue().toString();
                            String preso = dataSnapshot.child("preso").getValue().toString();
                            String aberto = dataSnapshot.child("aberto").getValue().toString();
                            arrayList.add(new Person(nomeDoPt,aberto,preso,"yzy"));
                            arrayAdapter.notifyDataSetChanged();
                            listView.setAdapter((arrayAdapter));

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        /*
        pontoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //These are all of your children.
                Map<String, Object> ponto2 = (Map<String, Object>) dataSnapshot.getValue();
                for (String childKey: ponto2.keySet()) {
                    Map<String, Object> atributosDoPonto = (Map<String, Object>) ponto2.get(childKey);
                    //String idDoPonto = (String) atributosDoPonto.get(atributosDoPonto.toString());
                    String idDoPonto = (String) atributosDoPonto.get(v);
                    Toast.makeText(Lista.this, idDoPonto,
                            Toast.LENGTH_SHORT).show();


                    DatabaseReference mDatabase;
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference  pontoRef = mDatabase.child("Usuario").child(auth.getCurrentUser().getUid()).child("Favorito");
                    pontoRef.addListenerForSingleValueEvent(new ValueEventListener() {





                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */
        //arrayList.add(new Person("GS1","gs","abretoo","yzy"));



        arrayAdapter.notifyDataSetChanged();
        listView.setAdapter((arrayAdapter));

    }
}
