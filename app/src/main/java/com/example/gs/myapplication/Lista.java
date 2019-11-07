package com.example.gs.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
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

import java.text.DecimalFormat;
import java.util.Map;
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
    private static DecimalFormat df2 = new DecimalFormat("#.##");





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
        //lista();
        DatabaseReference databaseDoc5;
        databaseDoc5 = FirebaseDatabase.getInstance().getReference();

        databaseDoc5.child("Ponto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();
                lista();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        //listview clicavel////


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String idS = String.valueOf(id);
                int idInt = Integer.parseInt(idS);

                MapsActivity.InfoSalvas.putString("key",  arrayList.get(idInt).getIdPt());

                Intent i = new Intent(Lista.this, InfoPonto.class);
                startActivity(i);
            }
        });




        arrayAdapter.notifyDataSetChanged();
        listView.setAdapter((arrayAdapter));

    }

    void lista (){
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

                    String teste = postSnapshot.getKey();



                    DatabaseReference mDatabase2;
                    mDatabase2 = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference  pontoRef2 = mDatabase2.child("Ponto").child(teste);
                    pontoRef2.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String latP = dataSnapshot.child("latiT").getValue().toString();
                            String longP = dataSnapshot.child("longT").getValue().toString();
                            Double LatPDb = Double.parseDouble(latP);
                            Double LongPDb = Double.parseDouble(longP);



                            Double lat  = 0.0;
                            Double  longi= 0.0;
                            Location location;


                            if (ActivityCompat.checkSelfPermission(Lista.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                            }else{

                                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                                if (location!= null){
                                    lat = location.getLatitude();
                                    longi = location.getLongitude();
                                }

                            }

                            float results []= new float[10];
                            Location.distanceBetween(lat, longi, LatPDb, LongPDb, results);
                            float distanciaEmKm = results[0]/1000;





                            String nomeDoPt = dataSnapshot.child("nome").getValue().toString();
                            String preso = dataSnapshot.child("preso").getValue().toString();
                            String aberto = dataSnapshot.child("aberto").getValue().toString();
                            String idDoPT = dataSnapshot.child("id").getValue().toString();
                            arrayList.add(new Person(nomeDoPt,aberto,"R$"+preso,df2.format(distanciaEmKm)+" Km", idDoPT));
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

    }



}
