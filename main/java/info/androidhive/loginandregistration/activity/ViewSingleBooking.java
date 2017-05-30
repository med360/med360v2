package info.androidhive.loginandregistration.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.helper.SessionManager;

public class ViewSingleBooking extends AppCompatActivity {

    private TextView txtbkdate;
    private TextView txtbktime;
    private TextView txtbkhpname;
    private TextView txtbkhpaddress;
    private TextView txtbkstatus;
    private Button bkcancelbtn;
    private NetworkImageView thumbNail;

    private SessionManager session;
    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Log.e("medlogin", "before redirecting to new activity on success login");
                    Intent intent = new Intent(ViewSingleBooking.this,
                            Viewdoctor.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.messg:
                    Log.e("medlogin", "before redirecting to new activity on success login");
                    Intent intent2 = new Intent(ViewSingleBooking.this,
                            ViewBooking.class);
                    startActivity(intent2);
                    finish();
                    return true;
                case R.id.profilenav:
                    Log.e("medlogin", "before redirecting to new activity on success login");
                    Intent intent3 = new Intent(ViewSingleBooking.this,
                            MainActivity.class);
                    startActivity(intent3);
                    finish();
                    return true;
                case R.id.logoutnav:
                    Log.e("medlogin", "before redirecting to new activity on success logout");
                    session = new SessionManager(getApplicationContext());
                    session.setLogin(false);
                    session.logoutUser();
                    return true;

            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
        setContentView(R.layout.activity_view_single_booking);


        txtbkdate=(TextView)findViewById(R.id.sbkdate);
        txtbktime=(TextView)findViewById(R.id.sbktime);
        txtbkhpname=(TextView)findViewById(R.id.sbkhospital);
        txtbkhpaddress=(TextView)findViewById(R.id.sbkhpaddress);
        txtbkstatus=(TextView)findViewById(R.id.sbkstatus);
        thumbNail = (NetworkImageView) findViewById(R.id.sbkthumbnail);

        bkcancelbtn=(Button) findViewById(R.id.bkcancelbtn) ;

        Intent i = getIntent();
        // getting doctor id (did) from intent
        final String reqid = i.getStringExtra("reqid");
        final String bkdate = i.getStringExtra("bkdate");
        final String bktimeinfo = i.getStringExtra("bktimeinfo");
        final String bkstatusinfo = i.getStringExtra("bkstatusinfo");
        final String bkhpaddress = i.getStringExtra("bkhpaddress");
        final String bkhospitalname = i.getStringExtra("bkhospitalname");
        final String bkthumbnail = i.getStringExtra("bkthumbnail");


        Log.e("hp", "single booking: passed hpaddress from viewbooking is : "+bkhpaddress);


        String stinfo=""+bkstatusinfo;
        if(stinfo.equals("PENDING")){

            txtbkstatus.setTextColor(Color.parseColor("#ffa800"));
            bkcancelbtn.setVisibility(View.VISIBLE);
        }
        else if(stinfo.equals("CONFIRMED")){
            bkcancelbtn.setVisibility(View.VISIBLE);
            txtbkstatus.setTextColor(Color.parseColor("#057100"));
        }
        else if(stinfo.equals("REJECTED")){
            bkcancelbtn.setVisibility(View.INVISIBLE);
            txtbkstatus.setTextColor(Color.parseColor("#ff0000"));
        }
        else if(stinfo.equals("CANCELLED")){
            bkcancelbtn.setVisibility(View.INVISIBLE);
            txtbkstatus.setTextColor(Color.parseColor("#ff0000"));
        }
        else if(stinfo.equals("COMPLETED")){
            bkcancelbtn.setVisibility(View.INVISIBLE);
            txtbkstatus.setTextColor(Color.parseColor("#000000"));
        }








        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ImageLoader imgLoader;
        imgLoader = AppController.getInstance().getImageLoader();
        thumbNail.setImageUrl(bkthumbnail, imgLoader);


        txtbkdate.setText(bkdate);
        txtbktime.setText(""+bktimeinfo);
        txtbkhpname.setText(bkhospitalname);
        txtbkhpaddress.setText(""+bkhpaddress);
        txtbkstatus.setText(bkstatusinfo);


        bkcancelbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                cancelappointment(reqid);


            }
        });


    }


    public void cancelappointment(final String reqid) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        Log.e("cancelbook", "in cancelappointment() before post");


        final String cancelbookingapi="http://azmediame.net/med360/webinterface/api/appointment_cancel.php";


        Log.e("cancelbook", "code to clear cache is below");

        AppController.getInstance().getRequestQueue().getCache().remove(cancelbookingapi);
        StringRequest strReq2 = new StringRequest(Request.Method.POST,cancelbookingapi, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("bookapp", "Register Response: " + response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // Request stored in MySQL

                       Toast.makeText(getApplicationContext(), "Booking Cancelled Successfully", Toast.LENGTH_LONG).show();
                        Intent in = new Intent(getApplicationContext(),
                                ViewBooking.class);
                        startActivityForResult(in, 100);


                    } else {

                        // Error occurred in registration. Get the error
                        // message
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
                Log.e("TAG", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("reqid", reqid);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq2, tag_string_req);
    }

}
