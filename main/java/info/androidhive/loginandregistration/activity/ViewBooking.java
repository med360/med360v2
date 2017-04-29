package info.androidhive.loginandregistration.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.adapter.BookingListAdapter;
import info.androidhive.loginandregistration.adapter.CustomListAdapter;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.helper.SessionManager;
import info.androidhive.loginandregistration.model.Appointment;
import info.androidhive.loginandregistration.model.Movie;

public class ViewBooking extends AppCompatActivity {

    private BookingListAdapter adapter;
    private SessionManager session;
    private List<Appointment> bookingList = new ArrayList<Appointment>();
    private static final String viewbookurl = "http://azmediame.net/med360/webinterface/api/get_patient_appointment.php";
    private static final String cancelbookingapi="http://azmediame.net/med360/webinterface/api/appointment_cancel.php";
    private ProgressDialog pDialog;
    private ListView listView;
    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Log.e("medlogin", "before redirecting to new activity on success login");
                    Intent intent = new Intent(ViewBooking.this,
                            Viewdoctor.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.messg:
                    Log.e("medlogin", "before redirecting to new activity on success login");
                    Intent intent2 = new Intent(ViewBooking.this,
                            Viewdoctor.class);
                    startActivity(intent2);
                    finish();
                    return true;
                case R.id.profilenav:
                    Log.e("medlogin", "before redirecting to new activity on success login");
                    Intent intent3 = new Intent(ViewBooking.this,
                            MainActivity.class);
                    startActivity(intent3);
                    finish();
                    return true;
                case R.id.logoutnav:
                    Log.e("medlogin", "before redirecting to new activity on success login");
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
        setContentView(R.layout.activity_view_booking);
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        HashMap<String, String> user = session.getUserDetails();
        String pid = user.get("pid");
        Log.e("viewbook", "Session PID is: "+pid);

        listView = (ListView) findViewById(R.id.bklist);
        adapter = new BookingListAdapter(this, bookingList);
        listView.setAdapter(adapter);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        get_patient_appointments(pid);

    }


    public void cancelappointment(final String reqid) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        Log.e("cancelbook", "in cancelappointment() before post");
        final String booking_api="http://azmediame.net/med360/webinterface/api/appointment_cancel.php";
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

                     //   Toast.makeText(getApplicationContext(), "Booking Cancelled Successfully", Toast.LENGTH_LONG).show();

                       adapter.notifyDataSetChanged();

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("message");
                     //   Toast.makeText(getApplicationContext(),
                           //     errorMsg, Toast.LENGTH_LONG).show();
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





    public void get_patient_appointments(final String pid){
        Log.e("viewbook", "before volley request");
        // changing action bar color


        // Creating volley request obj
        adapter.clearData();
        adapter.notifyDataSetChanged();
        String tag_string_req = "req_login";
        Log.e("viewbook", "code to clear cache is below");
        AppController.getInstance().getRequestQueue().getCache().remove(viewbookurl);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                viewbookurl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hidePDialog();
                try {

                    Log.e("viewbook", "inside onresponse");





                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");


                    if (success) {

                        JSONArray appointmentsarray = jObj.getJSONArray("appointments");


                        Log.e("viewbook", "inside onresponse: " + appointmentsarray);
// Parsing json
                        for (int i = 0; i < appointmentsarray.length(); i++) {
                            try {
                                Log.e("viewbook", "inside response loop");
                                JSONObject obj = appointmentsarray.getJSONObject(i);
                                Appointment booking = new Appointment();
                                booking.setDate(obj.getString("date"));
                                booking.setThumbnailUrl(obj.getString("image"));
                                if(obj.getString("ctime")==null){
                                    String ctime="";
                                    booking.setCtime(ctime);
                                }else{booking.setCtime(obj.getString("ctime"));}
                                booking.setPrefe(obj.getString("pref"));
                                booking.setReqid(obj.getString("reqid"));
                                booking.setStatus(obj.getString("status"));

                                // adding movie to movies array
                                bookingList.add(booking);
                                Log.e("viewbook", "added to list");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }else {
                        // Error in login. Get the error message
                        Log.e("viewbook", "error in listing doctor");
                        String errorMsg = jObj.getString("message");

                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();

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
                Log.e("viewbook", "inside getparams method ");
                Map<String, String> params = new HashMap<String, String>();
                params.put("pid", pid);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void logoutUser() {
        session.setLogin(false);
        session.logoutUser();
    }


}
