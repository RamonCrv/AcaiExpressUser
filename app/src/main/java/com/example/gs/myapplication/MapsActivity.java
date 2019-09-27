package com.example.gs.myapplication;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.gs.myapplication.ui.gallery.GalleryFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MapsActivity extends SupportMapFragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener {

    MapView mapView;
    private GoogleMap mMap;
    ArrayList Ponto = new ArrayList();

    @Override
   public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        criarPontos();
        LatLng latLng = new LatLng(0.025921, -51.068596);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.8f));
        //mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        mMap.setTrafficEnabled(false);
        mMap.setMinZoomPreference(10.0f);
        mMap.setMaxZoomPreference(17.5f);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setMyLocationEnabled(true);
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
        }



    }

    @Override
    public boolean onMyLocationButtonClick() {


        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTitle().equals("aqui")){

            Intent i = new Intent(getActivity(), lista.class);
            startActivity(i);
        }



        return true;
    }

    public void criarPontos(){
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference  pontoRef = mDatabase.child("Ponto");


        pontoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //These are all of your children.
                Map<String, Object> ponto2 = (Map<String, Object>) dataSnapshot.getValue();


                for (String childKey: ponto2.keySet()) {
                    //childKey is your "-LQka.. and so on"
                    //Your current object holds all the variables in your picture.
                    Map<String, Object> atributosDoPonto = (Map<String, Object>) ponto2.get(childKey);

                    String la = (String) atributosDoPonto.get("latiT");
                    String lo = (String) atributosDoPonto.get("longT");
                    float latitude = Float.parseFloat(la);
                    float longitude = Float.parseFloat(lo);
                    String nomeDoPonto = (String) atributosDoPonto.get("nome");
                    LatLng posNoMap = new LatLng(latitude, longitude);
                    final Marker ptMarker = mMap.addMarker(new MarkerOptions().position(posNoMap).title(nomeDoPonto).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_music_background2)));


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }






}
