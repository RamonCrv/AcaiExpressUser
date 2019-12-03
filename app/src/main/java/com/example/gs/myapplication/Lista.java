package com.example.gs.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
    private Uri mUri;
    private String url;
    ImageView imgPonto;
    Button voltar;

    private static DecimalFormat df2 = new DecimalFormat("#.##");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        auth = FirebaseAuth.getInstance();
       // Log.d(TAG, "onCreate: Started.");
        arrayList = new ArrayList<>();
        listView =(ListView) findViewById(R.id.listView);
        voltar = (Button) findViewById(R.id.ButtVoltarXML);


        arrayAdapter = new ArrayAdapter(this, R.layout.adapter_favorito, R.id.localname,arrayList) {

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView localname = (TextView) view.findViewById(R.id.localname);
                TextView Aberto = (TextView) view.findViewById(R.id.aberto);
                TextView naosei1 = (TextView) view.findViewById(R.id.textView4);
                TextView naosei2 = (TextView) view.findViewById(R.id.textView3);
                TextView nota = (TextView) view.findViewById(R.id.textView5);
                ImageView imgPonto = (ImageView) view.findViewById(R.id.ImgPt);
                localname.setText(arrayList.get(position).Name);
                Aberto.setText(arrayList.get(position).Sex);
                naosei1.setText(arrayList.get(position).Birthday);
                naosei2.setText(arrayList.get(position).Naosei0);
                nota.setText(arrayList.get(position).mednota);
                BuscarImg(imgPonto, arrayList.get(position).idPt);


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




        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String idS = String.valueOf(id);
                int idInt = Integer.parseInt(idS);
                MapsActivity.InfoSalvas.putString("key",  arrayList.get(idInt).getIdPt());
                finish();
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
                    Toast.makeText(Lista.this, "Você não possui postos favoritados no momento",Toast.LENGTH_SHORT).show();
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
                            String mednota = dataSnapshot.child("mediaAv").getValue().toString();
                            arrayList.add(new Person(nomeDoPt,aberto,"R$:"+preso,df2.format(distanciaEmKm)+" Km", idDoPT,mednota));
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

    public void BuscarImg( final ImageView img, String id){
        final String userID = auth.getCurrentUser().getUid();
        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("/Imagens/Ponto/").child(id);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                url = uri.toString();
                glide(url, img);
            }

        });
    }

    public void glide(String url, ImageView imagem){
        Glide.with(this).load(url).into(imagem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0){
            mUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap =  MediaStore.Images.Media.getBitmap(getContentResolver(),mUri);
                imgPonto.setImageDrawable(new BitmapDrawable(bitmap));
            } catch (IOException e) {
            }
        }
    }



}
