package com.example.gs.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Pop extends Activity {
    private EditText recCodAva;
    private Button ok;
    DatabaseReference databaseDoc2;
    @Override
    protected  void  onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.6));
        inicializarComponentes();
        clicksBotoes();
    }

    void inicializarComponentes(){
        recCodAva = (EditText) (findViewById(R.id.recCodAvaV));
        ok = (Button) (findViewById(R.id.okBut));

    }

    void clicksBotoes(){
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarCodAva(recCodAva.getText().toString());


            }
        });
    }

    void verificarCodAva(String CodAva){
        databaseDoc2 = FirebaseDatabase.getInstance().getReference();
        databaseDoc2.child("Ponto").orderByChild("codAva").equalTo(CodAva).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String str = MapsActivity.InfoSalvas.getString("key");
                if(dataSnapshot.exists() && dataSnapshot.child(str).child("id").exists()){

                    String  id = dataSnapshot.child(str).child("id").getValue().toString();

                    Intent i = new Intent(Pop.this, Notas.class);
                    startActivity(i);

                    Toast.makeText(Pop.this, "Avaliação autorizada, id: "+id,
                            Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(Pop.this, "Codigo incorreto",
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
