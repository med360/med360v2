package info.androidhive.loginandregistration.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.helper.SessionManager;

public class Booking extends AppCompatActivity {
    private Button btnrequest;
    private Button btndate;
    private Spinner preftime;
    private EditText appdate;
    private ProgressDialog pDialog;
    private SessionManager session;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    private EditText dateView;

    Spinner preftimespin;

    String[] preftimes = {
            "Morning",
            "Afternoon",
            "Evening",

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        btnrequest = (Button) findViewById(R.id.btnrequest);
        btndate = (Button) findViewById(R.id.btndate);
        appdate = (EditText) findViewById(R.id.appdate);
        preftime = (Spinner) findViewById(R.id.preftime);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }



        btnrequest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String appodate = appdate.getText().toString().trim();
                String ptime = preftime.getSelectedItem().toString().trim();

                Intent i = getIntent();
                // getting doctor id (did) from intent
                final String did = i.getStringExtra("did");



                // Fetching user details from Session
                HashMap<String, String> user = session.getUserDetails();
                Log.e("bookapp", "hashmap got returned and started to fetch details to strings");
                final String pid = user.get("pid");



                if (!appodate.isEmpty() && !ptime.isEmpty() ) {
                   makeappointmentrequest(pid, did, appodate, ptime);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter the date and preferred time!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        preftimespin = (Spinner)findViewById(R.id.preftime);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, preftimes);

        preftimespin.setAdapter(adapter2);
        preftimespin.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int position = preftimespin.getSelectedItemPosition();

                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                }
        );


        dateView = (EditText) findViewById(R.id.appdate);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

    }


    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    private void logoutUser() {
        session.setLogin(false);
        session.logoutUser();
    }



    private void makeappointmentrequest(final String pid, final String did, final String appodate, final String ptime) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Requesting ...");
        showDialog();
        Log.e("bookapp", "in makeappointment() before post");
        final String booking_api="http://azmediame.net/med360/webinterface/api/appointment_request.php";
        Log.e("bookapp", "code to clear cache is below");
        AppController.getInstance().getRequestQueue().getCache().remove(booking_api);
        StringRequest strReq2 = new StringRequest(Request.Method.POST,booking_api, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("bookapp", "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);

                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // Request stored in MySQL

                        Toast.makeText(getApplicationContext(), "Request Sent Successfully", Toast.LENGTH_LONG).show();

                        // Launch doctor profile activity
                        // Starting new intent
                        Intent in = new Intent(getApplicationContext(),
                                DoctorProfile.class);
                        // sending pid to next activity
                        in.putExtra("did", did);

                        // starting new activity and expecting some response back
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
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("pid", pid);
                params.put("did", did);
                params.put("date", appodate);
                params.put("pref", ptime);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq2, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
