package com.example.gs.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import io.opencensus.common.ServerStatsFieldEnums;

public class InfoPonto extends AppCompatActivity {
    String url;
    private  FirebaseAuth auth;

    FirebaseDatabase firebaseDatabase;
    private Button btnComent;
    private ImageView ImagemDoPonto;
    private TextView nomeDoPonto;
    private TextView StatusDoPonto;
    private TextView PrecoDoPonto;
    DatabaseReference databaseDoc2;
    public Button voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_ponto);
        Intent b = new Intent(InfoPonto.this, Carregando.class);
        startActivity(b);

        inicializarComponentes();
        BuscarImg();
        BuscarDoc();
        firebaseDatabase = FirebaseDatabase.getInstance();
        //String str = MapsActivity.InfoSalvas.getString("key");
       // Toast.makeText(this, str, Toast.LENGTH_SHORT).show();


        btnComent.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = firebaseDatabase.getReference("comente").child("comenterio").push();

            }
        }));

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void inicializarComponentes(){
        btnComent =(findViewById(R.id.btn_add));
        ImagemDoPonto= (findViewById(R.id.ImgPt));
        nomeDoPonto  = (findViewById(R.id.PontNomeXML));
        StatusDoPonto =  (findViewById(R.id.StatusXML));
        PrecoDoPonto = (findViewById(R.id.precoXML));
        voltar = (findViewById(R.id.ButtVoltarXML));
    }

    public void BuscarImg(){
        String str = MapsActivity.InfoSalvas.getString("key");
        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("/images/").child(str);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                url = uri.toString();
                glide(url,ImagemDoPonto);
            }

        });
    }
    //FUNÇÃO QUE BAIXA A IMAGEM E SALVA NO PONTO
    public void glide(String url,ImageView imagem){
        Glide.with(this).load(url).into(imagem);
    }

    public void BuscarDoc(){
        String str = MapsActivity.InfoSalvas.getString("key");
        databaseDoc2 = FirebaseDatabase.getInstance().getReference();
        databaseDoc2.child("Ponto").orderByChild("id").equalTo(str).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String str = MapsActivity.InfoSalvas.getString("key");
                    nomeDoPonto.setText(dataSnapshot.child(str).child("nome").getValue().toString());
                    PrecoDoPonto.setText(dataSnapshot.child(str).child("preso").getValue().toString());
                    StatusDoPonto.setText(dataSnapshot.child(str).child("aberto").getValue().toString());

                }else {

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }






}