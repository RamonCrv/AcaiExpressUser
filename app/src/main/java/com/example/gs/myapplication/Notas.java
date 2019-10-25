package com.example.gs.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

public class Notas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

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

                        break;
                    case SmileRating.GOOD:
                        Toast.makeText(Notas.this, "Bom!",Toast.LENGTH_SHORT).show();
                        break;
                    case SmileRating.GREAT:
                        Toast.makeText(Notas.this, "Excelente!!!",Toast.LENGTH_SHORT).show();
                        break;
                    case SmileRating.OKAY:
                        Toast.makeText(Notas.this, "Ok!",Toast.LENGTH_SHORT).show();
                        break;
                    case SmileRating.TERRIBLE:
                        Toast.makeText(Notas.this, "Horrivel!",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        smileRating.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
            @Override
            public void onRatingSelected(int level, boolean reselected) {
                Toast.makeText(Notas.this, "Seu Voto Foi :"+ level+" Estrelas",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
