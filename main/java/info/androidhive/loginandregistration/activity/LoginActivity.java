/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppConfig;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SessionManager;
import info.androidhive.loginandregistration.helper.SharedPrefManager;

public class LoginActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private ProgressDialog progressDialog;
    private SessionManager session;
    private SQLiteHandler db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);

        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler


        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Log.e("medlogin", "beginning to get email and password from text box in login screen");
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                Log.e("medlogin", "the email enter by user is : "+email+"");
                Log.e("medlogin", "the password enter by user is : "+password+"");
                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    Log.e("medlogin", "before checking validity of the user with email id : "+email+"");
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!- "+email+"", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) {

        Log.e("medlogin", "inside checklogin methos , the email passed is: "+email+"");
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();
        Log.e("medlogin", "before sending post request");
        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();
                Log.e("medlogin", "response received");
                try {
                    JSONObject jObj = new JSONObject(response);
                    Log.e("medlogin", "code to clear cache is below");
                    AppController.getInstance().getRequestQueue().getCache().remove(AppConfig.URL_LOGIN);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);
                        Log.e("medlogin", "inside the if condition when login is valid");
                        // Now store the user in SQLite
                       String uid = jObj.getString("uid");

                      JSONObject user = jObj.getJSONObject("user");
                        String utype = user.getString("utype");

                        if(utype.equals("2")){

                            String userid = user.getString("userid");
                            String created_at = user.getString("created_at");
                            String email = user.getString("email");
                            String did = user.getString("did");
                            String name = user.getString("name");
                            String image = user.getString("image");
                            session.createDoctorLoginSession(name, email,did,userid,utype,image);
                            Log.e("medlogin", "session created");
                            sendTokenToServer(email);
                        }

                        else

                            {

                                String name = user.getString("name");
                                String email = user.getString("email");
                                String nationality = user.getString("nationality");
                                String bgp = user.getString("bgp");

                                String dob = user.getString("dob");
                                String pid = user.getString("pid");
                                String userid = user.getString("userid");
                                String image = user.getString("image");
                                String created_at = user.getString("created_at");
                                Log.e("medlogin", "stored all user information to strings");



                                session.createLoginSession(name, email, dob,nationality,bgp,pid,userid,image,utype);
                                Log.e("medlogin", "session created");



                                Log.e("medlogin", "Before sendtokentoserver()");
                                sendTokenToServer(email);



                            }




                        // Inserting row in users table

                        // Launch main activity
                        Log.e("medlogin", "before redirecting to new activity on success login");
                        if(utype.equals("2")){


                            Intent intent = new Intent(LoginActivity.this,
                                    ViewChats.class);
                            startActivity(intent);
                            finish();



                        }else{

                            Intent intent = new Intent(LoginActivity.this,
                                    Viewdoctor.class);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        // Error in login. Get the error message
                        Log.e("medlogin", "error in login");
                    String errorMsg = jObj.getString("error_msg");

                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Log.e("medlogin", "inside getparams method and the email storing in hashmap for post are: "+email+"");
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }



    //storing token to mysql server
    private void sendTokenToServer(final String email) {
        Log.e("sendtoken", "inside sendtokentoserver()");
        //progressDialog = new ProgressDialog(this);
        //progressDialog.setMessage("Registering Device...");
        //progressDialog.show();
        final String tokenapi="http://azmediame.net/med360/webinterface/push/new/RegisterDevice.php";

        final String token = SharedPrefManager.getInstance(this).getDeviceToken();


        if (token == null) {
            Log.e("sendtoken", "inside token is null");
            //progressDialog.dismiss();
            Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, tokenapi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("sendtoken", "sendtoken api response: "+response);
                        //progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(LoginActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("token", token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
