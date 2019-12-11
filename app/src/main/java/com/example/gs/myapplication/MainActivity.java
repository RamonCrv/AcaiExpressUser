package com.example.gs.myapplication;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    private AppBarConfiguration mAppBarConfiguration;
    private Uri mUri;
    private String url;
    FirebaseAuth auth;
    private TextView nomeUser;
    private CircleImageView imgUser;
    public int eae;
    public static Bundle bundle1 = new Bundle();
    public static Bundle bundle2 = new Bundle();
    Double latitude;
    Double longitude;
    boolean priVezLogado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        inicialziarComponentes();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            BuscarImg();
            pegarNome();
        }else{
            porImagemDeslogado();
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setNavigationViewListener();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        fragmentManager   = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.nav_host_fragment, new MapsActivity(),"Maps fragiment" );
        transaction.commitAllowingStateLoss();
        getLocationn();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void singOut(){
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_Lista_de_Favoritos) {
            if (auth.getCurrentUser() != null) {
                Intent b = new Intent(this.getBaseContext(), Lista.class);
                startActivity(b);


                if(auth.getCurrentUser() != null){
                    }
                }else{
                Toast.makeText(this.getBaseContext(), "É necessario estar logado para acessar a lista de favoritos",
                        Toast.LENGTH_SHORT).show();
            }
            } else if (id == R.id.nav_sair){
            Intent y = new Intent(this, Login.class);
            startActivity(y);
            finish();
            if(auth.getCurrentUser() != null){
                Intent g = new Intent(this, Login.class);
                startActivity(g);
                singOut();
                finish();
            }
            if(auth.getCurrentUser() == null){

            }else{
                Toast.makeText(this.getBaseContext(), "É necessario estar logado para Sair",
                        Toast.LENGTH_SHORT).show();
            }

            }else if (id == R.id.nav_sobre){


             }else if (id == R.id.nav_config){


            if(auth.getCurrentUser() != null) {
                Intent g = new Intent(this, userConfig2.class);
                startActivity(g);
               // finish();
            }else{
                Toast.makeText(this.getBaseContext(), "É necessario estar logado para confirurar conta",
                        Toast.LENGTH_SHORT).show();
            }

        }
            DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

    public void BuscarImg(){
        FirebaseAuth auth2 = FirebaseAuth.getInstance();
        final String userID = auth2.getCurrentUser().getUid();
        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("/Imagens/Usuario/").child(userID);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                url = uri.toString();
               glide(url, imgUser);
            }

        });
    }

    public void glide(String url, CircleImageView imagem){
        Glide.with(this).load(url).into(imagem);
    }

    void inicialziarComponentes(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        imgUser =  headerView.findViewById(R.id.ImgUserConf);
        nomeUser = headerView.findViewById(R.id.nomeUserConf);
    }

    void pegarNome(){
        final DatabaseReference databaseDoc5;
        databaseDoc5 = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        databaseDoc5.child("Usuario/"+auth.getCurrentUser().getUid()+"/Nome").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    nomeUser.setText(dataSnapshot.getValue().toString());
                    getLocationn();
                }else{
                    priVezLogado = true;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onBackPressed() {

    }

    void porImagemDeslogado(){
        imgUser.setImageResource(R.mipmap.imgdeslogado);
    }

    void getLocationn(){
        LocationManager locationManager;
        Location locationNow;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            MainActivity.bundle1.putString("latitude", "123123");
        }else{
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationNow = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //MainActivity.bundle1.putString("latitude", "123123");
           if(locationNow != null){
                latitude = locationNow.getLatitude();
                longitude = locationNow.getLongitude();
                MainActivity.bundle1.putDouble("latitude", latitude);
                MainActivity.bundle2.putDouble("longitude", longitude);
           }



        }

    }



    }




