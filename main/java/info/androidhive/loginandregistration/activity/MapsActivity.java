package info.androidhive.loginandregistration.activity;


import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.model.Filtervalues;
import info.androidhive.loginandregistration.model.Movie;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback  {


private String url="http://azmediame.net/med360/webinterface/api/get_markers.php";
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

get_all_markers(googleMap);


    }





    public void get_all_markers(GoogleMap googleMap){
        Log.e("dsp", "before volley request");
        // changing action bar color
        mMap = googleMap;

        // Creating volley request obj
        String tag_string_req = "req_login";
        Log.e("medlogin", "code to clear cache is below");
        AppController.getInstance().getRequestQueue().getCache().remove(url);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    Log.e("dsp", "inside onresponse");





                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");


                    if (success) {

                        JSONArray doctorarray = jObj.getJSONArray("doctor");


                        Log.e("dsp", "inside onresponse: " + doctorarray);
// Parsing json
                        for (int i = 0; i < doctorarray.length(); i++) {
                            try {
                                Log.e("dsp", "inside response loop");
                                JSONObject obj = doctorarray.getJSONObject(i);
                                Double lat=obj.getDouble("latitude");
                                Double lng=obj.getDouble("longitude");
                                String hpname=obj.getString("hospitalname");
                                String dname=obj.getString("dname");
                                Log.e("getmarkers", "latitude is :"+lat+" longitude is: "+lng);

                               LatLng hp = new LatLng(lat,lng);
                                mMap.setMaxZoomPreference(14);
                                mMap.addMarker(new MarkerOptions()
                                        .position(hp)
                                        .title(dname));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(hp));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }else {
                        // Error in login. Get the error message
                        Log.e("medlogin", "error in listing doctor");
                        String errorMsg = jObj.getString("message");

                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Doctor receive Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }

        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Log.e("medlogin", "inside getparams method ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("latit", "");
                params.put("longit", "");

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }








}




  /*  public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(10, 10))
                .title("Hello world"));
    }*/

