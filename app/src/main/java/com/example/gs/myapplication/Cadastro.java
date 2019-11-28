package com.example.gs.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Cadastro extends AppCompatActivity {
    private Button btnRegistrar;
    private EditText editEmail;
    private EditText editSenha;
    private FirebaseAuth mAuth;
    private EditText conSenha;
    public Button voltar;
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);



        inicializarComponentes();
        eventoClicks();
        mAuth = FirebaseAuth.getInstance();

    }
    //EVENTOS DE BOTÕES
    private void eventoClicks() {
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Cadastro.this, Login.class);
                startActivity(i);
                finish();
            }
        });
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString().trim();
                String senha = editSenha.getText().toString().trim();
                criarUser(email,senha);
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
                    alert("Prencha Todos os Campos!");
                }else {
                    btnRegistrar.setVisibility(View.INVISIBLE);
                }
            }
        });

    }
    //CRIAR USUARIO TRADICIONAL
    private void criarUser(String email, String senha){

        if(email.contains("@") == false){
            alert("ENTRE COM UM EMAIL VALIDO!!!");
            btnRegistrar.setVisibility(View.VISIBLE);
        }else{
            if (senha.length() <=7 ){
                alert("SENHA MUITO PEQUENA");
                btnRegistrar.setVisibility(View.VISIBLE);

            }else{
                if (editSenha.getText().toString().trim().equals(conSenha.getText().toString().trim()) == false){
                    alert("AS SENHAS NÃO COMBINAM");
                    btnRegistrar.setVisibility(View.VISIBLE);
                }else{
                    mAuth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener(Cadastro.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete( Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                mAuth.getCurrentUser();
                                alert("Cadastrado com Sucesso!");
                                Intent i =new Intent(Cadastro.this, MainActivity.class);
                                startActivity(i);
                            }else{
                                alert("Email já Cadastrado");
                                btnRegistrar.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        }
    }

    //INICIALIZA COMPONENTES
    private void inicializarComponentes() {
        btnRegistrar = (Button) findViewById(R.id.btCadastrar);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editSenha = (EditText) findViewById(R.id.editSenha);
        conSenha = ((EditText) findViewById(R.id.ConfSenha));
        voltar = (findViewById(R.id.btnVoltar));
    }
    //MOSTRA MSG
    private  void alert (String msg){
        final Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        Toast.makeText(Cadastro.this,msg,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(Cadastro.this, Login.class);
        startActivity(i);
        finish();
    }
}

