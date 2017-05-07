package info.androidhive.loginandregistration.activity;


import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import org.json.JSONArray;
import org.json.JSONObject;

import info.androidhive.loginandregistration.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback  {



    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        LatLng NMC = new LatLng(25.29000,55.3692);
        LatLng alzahra = new LatLng (25.1058,55.1806);
        LatLng Zulekha = new LatLng(25.3688,55.4047);
        LatLng Aster = new LatLng(25.3886,55.4609);
        LatLng MedCare = new LatLng(25.1833,55.2427);
        LatLng Universal = new LatLng(24.4598,54.3791);
        LatLng Brightpoint = new LatLng(24.4524, 54.3933);


        mMap.addMarker(new MarkerOptions()
                .position(NMC)
                .title("New Medical Center"));

        mMap.addMarker(new MarkerOptions()
                .position(alzahra)
                .title("Al Zahra Hospital"));

        mMap.addMarker(new MarkerOptions()
                .position(Zulekha)
                .title("Zulekha Hospital"));

        mMap.addMarker(new MarkerOptions()
                .position(Aster)
                .title("Aster Medical Center"));

        mMap.addMarker(new MarkerOptions()
                .position(MedCare)
                .title("Medcare Hospital"));

        mMap.addMarker(new MarkerOptions()
                .position(Universal)
                .title("Universal Hospital"));

        mMap.addMarker(new MarkerOptions()
                .position(Brightpoint)
                .title("Brightpoint Royal Women Hospital"));









        mMap.moveCamera(CameraUpdateFactory.newLatLng(NMC));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(alzahra));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Zulekha));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Aster));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(MedCare));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Universal));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Brightpoint));




    }

}




  /*  public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(10, 10))
                .title("Hello world"));
    }*/

