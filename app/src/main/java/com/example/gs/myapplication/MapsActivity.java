package com.example.gs.myapplication;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import com.example.gs.myapplication.utils.Tools;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapsActivity extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener  {
    public MapView mapView;
    public GoogleMap mMap;



    public TextView mInputDisplay;
    private static final String ALLOWED_CHARACTERS ="0123456789QWERTYUIOPASDFGHJKLZXCVBNM";
    private static final String TAG = "MapsActivity";
    public static Bundle InfoSalvas = new Bundle();
    public static Bundle userLat = new Bundle();
    public static Bundle userLong= new Bundle();




    ArrayList Ponto = new ArrayList();
    private Location location;
    private LocationManager locationManager;

    @Override
   public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);


    }


    private CameraUpdate zoomingLocation() {
        return CameraUpdateFactory.newLatLngZoom(new LatLng(37.76496792, -122.42206407), 13);
    }




    @Override
    public void onMapReady(final GoogleMap googleMap) {


        try {

            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.style_map));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);

        }


        //googleMap.moveCamera(CameraUpdateFactory.newCameraPosition();
        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 30);


        }else{

        }
        mMap = googleMap;
        criarPontos(googleMap);


        //ATUALIZA OS PONTOS EM TEMPO REAL.
        DatabaseReference databaseDoc5;
        databaseDoc5 = FirebaseDatabase.getInstance().getReference();

        databaseDoc5.child("Ponto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                googleMap.clear();
                criarPontos(googleMap);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Toast.makeText(getActivity(), "Latitude: "+locationB.getDouble("latitude")+" e Longitude: "+  locationB.getDouble("longitude"), Toast.LENGTH_SHORT).show();



        mMap.setTrafficEnabled(false);
        mMap.setMinZoomPreference(13.0f);
        mMap.setMaxZoomPreference(17.5f);
        mMap.setIndoorEnabled(true);
        mMap.getFocusedBuilding();
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setMyLocationEnabled(true);
        //mMap.setPadding(0, 0, 30, 30);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }


            LatLng latLng = new LatLng(MainActivity.bundle1.getDouble("latitude"), MainActivity.bundle2.getDouble("longitude"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.4f));




    }




    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
    @Override
    public void onMyLocationClick(@NonNull Location location) {
    }

    public void criarPontos(GoogleMap googleMap){
        mMap = googleMap;
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference  pontoRef = mDatabase.child("Ponto");
        pontoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //These are all of your children.
                Map<String, Object> ponto2 = (Map<String, Object>) dataSnapshot.getValue();
                for (String childKey: ponto2.keySet()) {
                    Map<String, Object> atributosDoPonto = (Map<String, Object>) ponto2.get(childKey);
                    String la = (String) atributosDoPonto.get("latiT");
                    String lo = (String) atributosDoPonto.get("longT");
                    String st = (String) atributosDoPonto.get("aberto");
                    float latitude = Float.parseFloat(la);
                    float longitude = Float.parseFloat(lo);
                    String title = (String) atributosDoPonto.get("id");
                    LatLng posNoMap = new LatLng(latitude, longitude);
                    if(st.equals("Aberto")){

                        final Marker ptMarker = mMap.addMarker(new MarkerOptions().position(posNoMap).title(title).icon(BitmapDescriptorFactory.fromResource(R.mipmap.markon)));


                    }else{
                        final Marker ptMarker = mMap.addMarker(new MarkerOptions().position(posNoMap).title(title).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ponto_fechado_background)));

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String key = marker.getTitle();
                MapsActivity.InfoSalvas.putString("key", key);
                Intent i = new Intent(getActivity(),InfoPonto.class);
               startActivity(i);
             //  getActivity().finish();
                return true;

            }
        });

    }








}


