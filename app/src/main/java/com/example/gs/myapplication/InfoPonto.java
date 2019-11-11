package com.example.gs.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

public class InfoPonto extends AppCompatActivity {
    String url;
    private  FirebaseAuth auth;

    FirebaseDatabase firebaseDatabase;
    private Button btnComent;
    private ImageButton btnestar;
    private ImageView ImagemDoPonto;
    private TextView nomeDoPonto;
    private TextView mednota;
    private TextView StatusDoPonto;
    private TextView PrecoDoPonto;
    public String idDoPonto;
    DatabaseReference databaseDoc2;
    DatabaseReference databaseDoc3;
    public Button button;
    public Button voltar;

    private boolean taFavoritado;
    public  boolean eFavorito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_ponto);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.color3));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Intent b = new Intent(InfoPonto.this, Carregando.class);
        startActivity(b);
        inicializarComponentes();
        BuscarImg();
        BuscarDoc();
        DatabaseReference databaseDoc5;
        databaseDoc5 = FirebaseDatabase.getInstance().getReference();
        databaseDoc5.child("Ponto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               BuscarDoc();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            verificarFavoritos();
        }
        eventoClicks();

        //String str = MapsActivity.InfoSalvas.getString("key");
       // Toast.makeText(this, str, Toast.LENGTH_SHORT).show();


        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void eventoClicks() {

        btnComent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth.getCurrentUser() != null){
                    startActivity(new Intent(InfoPonto.this,Pop.class));
                }else {
                    Toast.makeText(InfoPonto.this, "É necessario estar logado para avaliar um ponto.",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });

       btnestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auth.getCurrentUser() != null){
                    if (eFavorito){
                     Desfavoritar();
                        btnestar.setBackgroundResource(R.mipmap.estrelaoff_background);
                        eFavorito = !eFavorito;
                    }else{
                        salvarNosFavoritos();
                        Toast.makeText(InfoPonto.this, "Posto Favoritado",
                                Toast.LENGTH_SHORT).show();
                        eFavorito = !eFavorito;
                        btnestar.setBackgroundResource(R.mipmap.estrelaon_background);
                    }
                }else {
                    Toast.makeText(InfoPonto.this, "É necessario estar logado para favoritar",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void inicializarComponentes(){
        btnComent =(findViewById(R.id.button20));
        ImagemDoPonto= (findViewById(R.id.ImgPt));
        mednota = (findViewById(R.id.mednota));
        nomeDoPonto  = (findViewById(R.id.PontNomeXML));
        StatusDoPonto =  (findViewById(R.id.StatusXML));
        PrecoDoPonto = (findViewById(R.id.precoXML));
        voltar = (findViewById(R.id.ButtVoltarXML));
        btnestar = (findViewById(R.id.btnestar));



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
                    idDoPonto = str;
                    nomeDoPonto.setText(dataSnapshot.child(str).child("nome").getValue().toString());
                     mednota.setText(dataSnapshot.child(str).child("mediaAv").getValue().toString());
                    PrecoDoPonto.setText("R$:"+dataSnapshot.child(str).child("preso").getValue().toString()+"/L");
                    StatusDoPonto.setText(dataSnapshot.child(str).child("aberto").getValue().toString());

                }else {
                    Toast.makeText(InfoPonto.this, "erro ao carregar dados",
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void salvarNosFavoritos(){
        databaseDoc3 = FirebaseDatabase.getInstance().getReference("Usuario");
        databaseDoc3.child(auth.getCurrentUser().getUid()).child("Favorito").child(idDoPonto).setValue(idDoPonto);
    }

    public  void Desfavoritar(){
        databaseDoc3 = FirebaseDatabase.getInstance().getReference("Usuario");
        databaseDoc3.child(auth.getCurrentUser().getUid()).child("Favorito").child(idDoPonto).removeValue();
    }

 public void verificarFavoritos(){
        String str = MapsActivity.InfoSalvas.getString("key");
        eFavorito = false;
        btnestar.setBackgroundResource(R.mipmap.estrelaoff_background);
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuario/"+auth.getCurrentUser().getUid()+"/Favorito/"+str);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //These are all of your children.
               if (dataSnapshot.exists()){
                   eFavorito = true;
                   btnestar.setBackgroundResource(R.mipmap.estrelaon_background);
               }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}