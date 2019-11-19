package com.example.gs.myapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

public class Notas extends AppCompatActivity {
    DatabaseReference databaseDoc2;
    DatabaseReference databaseDoc;
    Button avaliar;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    float notaDoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);
        auth = FirebaseAuth.getInstance();
        SmileRating smileRating = (SmileRating) findViewById(R.id.smile_rating);
        smileRating.setNameForSmile(BaseRating.BAD,"Ruim") ;
        smileRating.setNameForSmile(BaseRating.GOOD,"Bom") ;
        smileRating.setNameForSmile(BaseRating.GREAT,"Excelente") ;
        smileRating.setNameForSmile(BaseRating.OKAY,"OK") ;
        smileRating.setNameForSmile(BaseRating.TERRIBLE,"Horrivel") ;


        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {

                switch (smiley) {
                    case SmileRating.BAD:

                        notaDoUser = 2;
                        break;
                    case SmileRating.GOOD:

                        notaDoUser = 4;
                        break;
                    case SmileRating.GREAT:

                        notaDoUser = 5;
                        break;
                    case SmileRating.OKAY:

                        notaDoUser = 3;
                        break;
                    case SmileRating.TERRIBLE:

                        notaDoUser = 1;
                        break;
                }
            }
        });
        smileRating.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
            @Override
            public void onRatingSelected(int level, boolean reselected) {

            }
        });
        clicks();

    }

    void  clicks(){
        avaliar = (Button) findViewById(R.id.enviar);
        avaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addAv();
                alert("Obrigado por avaliar");
                Intent i = new Intent(Notas.this, InfoPonto.class);
                startActivity(i);
                finish();
            }
        });
    }

    void addAv(){
        String str = MapsActivity.InfoSalvas.getString("key");
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuario/"+auth.getCurrentUser().getUid()+"/PontosAvaliados/"+str);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //These are all of your children.
                if (dataSnapshot.exists()){
                    String str = MapsActivity.InfoSalvas.getString("key");
                    databaseDoc2 = FirebaseDatabase.getInstance().getReference();
                    databaseDoc2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                String str = MapsActivity.InfoSalvas.getString("key");
                                float vtav = Float.parseFloat(dataSnapshot.child("Ponto/"+str+"/somaAv").getValue().toString());
                                float tdav = Float.parseFloat(dataSnapshot.child("Ponto/"+str+"/totalAv").getValue().toString());

                               int notaAtual = Integer.parseInt(dataSnapshot.child("Usuario/"+auth.getCurrentUser().getUid()+"/PontosAvaliados/"+str+"/Nota").getValue().toString());

                                vtav = vtav - notaAtual;
                                vtav+=notaDoUser;

                                float media= vtav/tdav;
                                String mediaS = String.valueOf(media);
                                String tdavS = String.valueOf(tdav);
                                String vtavS = String.valueOf(vtav);
                                salvarNosFavoritos(str);
                                FirebaseDatabase.getInstance().getReference().child("Ponto").child(str).child("totalAv").setValue(tdavS);
                                FirebaseDatabase.getInstance().getReference().child("Ponto").child(str).child("somaAv").setValue(vtavS);
                                FirebaseDatabase.getInstance().getReference().child("Ponto").child(str).child("mediaAv").setValue(mediaS);

                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else{
                    String str = MapsActivity.InfoSalvas.getString("key");
                    databaseDoc2 = FirebaseDatabase.getInstance().getReference();
                    databaseDoc2.child("Ponto").orderByChild("id").equalTo(str).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                String str = MapsActivity.InfoSalvas.getString("key");
                                int tdav = Integer.parseInt(dataSnapshot.child(str).child("totalAv").getValue().toString());
                                float vtav = Float.parseFloat(dataSnapshot.child(str).child("somaAv").getValue().toString());
                                vtav+=notaDoUser;
                                tdav+=1;
                                float media= vtav/tdav;
                                String mediaS = String.valueOf(media);
                                String tdavS = String.valueOf(tdav);
                                String vtavS = String.valueOf(vtav);
                                salvarNosFavoritos(str);
                                FirebaseDatabase.getInstance().getReference().child("Ponto").child(str).child("totalAv").setValue(tdavS);
                                FirebaseDatabase.getInstance().getReference().child("Ponto").child(str).child("somaAv").setValue(vtavS);
                                FirebaseDatabase.getInstance().getReference().child("Ponto").child(str).child("mediaAv").setValue(mediaS);

                            }
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

    public void salvarNosFavoritos(String idDoPonto){
        DatabaseReference databaseDoc3;

        databaseDoc3 = FirebaseDatabase.getInstance().getReference("Usuario");
        databaseDoc3.child(auth.getCurrentUser().getUid()).child("PontosAvaliados").child(idDoPonto).child("Nota").setValue(notaDoUser);
    }

    private  void alert (String msg){
        final Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        Toast.makeText(Notas.this,msg,Toast.LENGTH_SHORT).show();
    }





}
