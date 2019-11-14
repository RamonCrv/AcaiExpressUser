package com.example.gs.myapplication;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    private FragmentManager fragmentManager;
    private AppBarConfiguration mAppBarConfiguration;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Intent i = new Intent(this, Carregando.class);
        startActivity(i);
        setNavigationViewListener();
        auth = FirebaseAuth.getInstance();
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
            if(auth.getCurrentUser() != null){
                Intent g = new Intent(this, Login.class);
                startActivity(g);
                singOut();
            }
            if(auth.getCurrentUser() == null){

            }else{
                Toast.makeText(this.getBaseContext(), "É necessario estar logado para Sair",
                        Toast.LENGTH_SHORT).show();
            }

            }else if (id == R.id.nav_sobre){


             }else if (id == R.id.nav_config){
            if(auth.getCurrentUser() != null){
            }else{
                Toast.makeText(this.getBaseContext(), "É necessario estar logado para confirurar conta",
                        Toast.LENGTH_SHORT).show();
            }

        }
            DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
       // return true;
    }




