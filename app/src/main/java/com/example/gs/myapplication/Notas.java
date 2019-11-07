package com.example.gs.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
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
                        Toast.makeText(Notas.this, "Ruim!",Toast.LENGTH_SHORT).show();
                        notaDoUser = 2;
                        break;
                    case SmileRating.GOOD:
                        Toast.makeText(Notas.this, "Bom!",Toast.LENGTH_SHORT).show();
                        notaDoUser = 4;
                        break;
                    case SmileRating.GREAT:
                        Toast.makeText(Notas.this, "Excelente!!!",Toast.LENGTH_SHORT).show();
                        notaDoUser = 5;
                        break;
                    case SmileRating.OKAY:
                        Toast.makeText(Notas.this, "Ok!",Toast.LENGTH_SHORT).show();
                        notaDoUser = 3;
                        break;
                    case SmileRating.TERRIBLE:
                        Toast.makeText(Notas.this, "Horrivel!",Toast.LENGTH_SHORT).show();
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

                                float tdav = Float.parseFloat(dataSnapshot.child("Ponto/"+str+"/TotalAv").getValue().toString());
                                float vtav = Float.parseFloat(dataSnapshot.child("Ponto/"+str+"/SomaAv").getValue().toString());

                               int notaAtual = Integer.parseInt(dataSnapshot.child("Usuario/"+auth.getCurrentUser().getUid()+"/PontosAvaliados/"+str+"/Nota").getValue().toString());

                                vtav = vtav - notaAtual;
                                vtav+=notaDoUser;

                                float media= vtav/tdav;
                                String mediaS = String.valueOf(media);
                                String tdavS = String.valueOf(tdav);
                                String vtavS = String.valueOf(vtav);
                                salvarNosFavoritos(str);
                                FirebaseDatabase.getInstance().getReference().child("Ponto").child(str).child("TotalAv").setValue(tdavS);
                                FirebaseDatabase.getInstance().getReference().child("Ponto").child(str).child("SomaAv").setValue(vtavS);
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
                                float tdav = Float.parseFloat(dataSnapshot.child(str).child("TotalAv").getValue().toString());
                                float vtav = Float.parseFloat(dataSnapshot.child(str).child("SomaAv").getValue().toString());
                                vtav+=notaDoUser;
                                tdav+=1;
                                float media= vtav/tdav;
                                String mediaS = String.valueOf(media);
                                String tdavS = String.valueOf(tdav);
                                String vtavS = String.valueOf(vtav);
                                salvarNosFavoritos(str);
                                FirebaseDatabase.getInstance().getReference().child("Ponto").child(str).child("TotalAv").setValue(tdavS);
                                FirebaseDatabase.getInstance().getReference().child("Ponto").child(str).child("SomaAv").setValue(vtavS);
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


}
