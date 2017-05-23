package info.androidhive.loginandregistration.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import info.androidhive.loginandregistration.R;
import info.androidhive.loginandregistration.app.AppController;

public class MessagingActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private Button btnsend;
    private EditText txtmssg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        btnsend = (Button) findViewById(R.id.btnsend);
        txtmssg = (EditText) findViewById(R.id.medmssg);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        Intent i = getIntent();
        // getting doctor id (did) from intent

        final String puid = i.getStringExtra("puid");
        final String duid = i.getStringExtra("duid");





        btnsend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final String message = txtmssg.getText().toString();
                Log.e("mssg", "the message type is: "+message);
              sendmssg(puid,duid,message);
            }
        });
    }


    private void sendmssg(final String puid, final String duid, final String mssg) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Requesting ...");
        showDialog();
        Log.e("mssg", "in sendmssg() before post");
        final String sendmssg_api="http://azmediame.net/med360/webinterface/api/send_message.php";
        Log.e("bookapp", "code to clear cache is below");
        AppController.getInstance().getRequestQueue().getCache().remove(sendmssg_api);
        StringRequest strReq2 = new StringRequest(Request.Method.POST,sendmssg_api, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("mssg", "Send Message Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);

                    //boolean error = jObj.getBoolean("error");
                    String success = jObj.getString("success");
                    if (success.equals("1")) {
                        // Request stored in MySQL

                        Toast.makeText(getApplicationContext(), "Message Sent Successfully", Toast.LENGTH_LONG).show();

                        // Launch doctor profile activity
                        // Starting new intent
                        Intent in = new Intent(getApplicationContext(),
                                ConversationActivity.class);
                        // sending pid to next activity
                        in.putExtra("puid", puid);
                        in.putExtra("duid", duid);

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
                params.put("sender_id", puid);
                params.put("reciepent_id", duid);
                params.put("message", mssg);

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




