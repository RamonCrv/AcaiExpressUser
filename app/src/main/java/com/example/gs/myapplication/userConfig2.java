package com.example.gs.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gs.myapplication.utils.Tools;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class userConfig2 extends AppCompatActivity {


    private CircleImageView imgUser;
    private Button ediImg;
    private Button salvar;
    private EditText nomeUser;
    private Uri mUri;
    private String url;
    private FirebaseAuth auth;
    private boolean trocouImagem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_user_config2);
        inicializarComponentes();
        clicks();
        auth = FirebaseAuth.getInstance();
        BuscarImg();
        buscarNome();
    }
    void inicializarComponentes(){
        imgUser = (findViewById(R.id.circleImageView));
        ediImg = (findViewById(R.id.tbEdit));
        salvar = (findViewById(R.id.btSalvar));
        nomeUser = (findViewById(R.id.nomeUserConf));


    }

    void clicks(){



        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trocouImagem){
                    saveUserInFirebase();
                }
                if (nomeUser.getText().toString() != null){
                    Toast.makeText(userConfig2.this, "Atualizado Com Sucesso",
                            Toast.LENGTH_SHORT).show();
                    salvarNome();
                }
                trocouImagem = false;

            }
        });

        ediImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectfoto();
                trocouImagem = true;


            }
        });

    }



    private void selectfoto(){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 0);

    }
    private void saveUserInFirebase() {
        String userID = auth.getCurrentUser().getUid();
        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("/Imagens/Usuario/"+userID);
        ref.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.i("teste",uri.toString());
                    }
                });
            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("test", e.getMessage(), e);
            }
        });
    }

    public void BuscarImg(){
        final String userID = auth.getCurrentUser().getUid();
        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("/Imagens/Usuario/").child(userID);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                url = uri.toString();
                glide(url, imgUser);
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
                imgUser.setImageDrawable(new BitmapDrawable(bitmap));
            } catch (IOException e) {
            }
        }
    }

    void salvarNome(){
        DatabaseReference databaseDoc3;

        databaseDoc3 = FirebaseDatabase.getInstance().getReference("Usuario");
        databaseDoc3.child(auth.getCurrentUser().getUid()).child("Nome").setValue(nomeUser.getText().toString());
    }

    void buscarNome(){
        final DatabaseReference databaseDoc5;
        databaseDoc5 = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        databaseDoc5.child("Usuario/"+auth.getCurrentUser().getUid()+"/Nome").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    nomeUser.setText(dataSnapshot.getValue().toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}
