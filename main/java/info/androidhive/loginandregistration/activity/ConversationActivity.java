package info.androidhive.loginandregistration.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
import info.androidhive.loginandregistration.adapter.ChatListAdapter;
import info.androidhive.loginandregistration.adapter.ConvoListAdapter;
import info.androidhive.loginandregistration.app.AppController;
import info.androidhive.loginandregistration.helper.SessionManager;
import info.androidhive.loginandregistration.model.Appointment;
import info.androidhive.loginandregistration.model.Chats;
import info.androidhive.loginandregistration.model.Convos;

public class ConversationActivity extends AppCompatActivity {

    private ConvoListAdapter adapter;
    private SessionManager session;
    private List<Convos> cnList = new ArrayList<Convos>();
    private static final String convosurl = "http://azmediame.net/med360/webinterface/api/get_rec_message.php";
private Button sendbtn;
    private FloatingActionButton btnsend;
    private EditText chtxtinput;
    private ProgressDialog pDialog;
    private ListView listView;
    private String ptid="";
    private String dtid="";
    private String session_uid="";
    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Log.e("medlogin", "before redirecting to new activity on success login");
                    Intent intent = new Intent(ConversationActivity.this,
                            Viewdoctor.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.messg:
                    Log.e("medlogin", "before redirecting to new activity on success login");
                    Intent intent2 = new Intent(ConversationActivity.this,
                            ViewBooking.class);
                    startActivity(intent2);
                    finish();
                    return true;
                case R.id.profilenav:
                    Log.e("medlogin", "before redirecting to new activity on success login");
                    Intent intent3 = new Intent(ConversationActivity.this,
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
        setContentView(R.layout.activity_conversation);

        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        HashMap<String, String> user = session.getUserDetails();
        String pid = user.get("pid");
        String userid = user.get("userid");
        final String name = user.get("name");
        session_uid=userid;
        Log.e("convo", "Session PID is: "+pid);
        Log.e("convo", "Session USERID is: "+user);

        listView = (ListView) findViewById(R.id.cnlist);
        adapter = new ConvoListAdapter(this, cnList);
        listView.setAdapter(adapter);

        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(adapter.getCount() - 1);
            }
        });
        Intent i = getIntent();
        final String puid = i.getStringExtra("puid");
        final String duid = i.getStringExtra("duid");
        ptid=puid;
        dtid=duid;
        btnsend= (FloatingActionButton) findViewById(R.id.btnsend) ;
        chtxtinput =  (EditText) findViewById(R.id.chtxtinput);


        chtxtinput.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                   String message = chtxtinput.getText().toString();
                    chtxtinput.setText("");
                    return sendChatMessage(puid,duid,message,name);
                }
                return false;
            }
        });



        btnsend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final String message = chtxtinput.getText().toString();
                Log.e("mssg", "the message type is: "+message);
                Convos con = new Convos();
                con.setCnmssg(message);
                con.setCnsndname(name+":");
                cnList.add(con);
                adapter.notifyDataSetChanged();

                adapter.registerDataSetObserver(new DataSetObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        Log.e("adapterobserver", "SEND: Inside onChanged()");
                        listView.setSelection(adapter.getCount() - 1);
                    }
                });


                sendmssg(puid,duid,message);
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(adapter.getCount() - 1);
            }
        });



//sendbtn= (Button) findViewById(R.id.newmssgbtn) ;



        //sendbtn.setOnClickListener(new View.OnClickListener() {
            //public void onClick(View view) {
                // Starting new intent
              //  Intent in = new Intent(getApplicationContext(),
              //          MessagingActivity.class);
                // sending did to next activity
              //  in.putExtra("puid", puid);
              //  in.putExtra("duid", duid);

                // starting new activity and expecting some response back
               // startActivityForResult(in, 100);
           // }
       // });

        //listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

           // @Override
           // public void onItemClick(AdapterView<?> parent, View view,
            //                        int position, long id) {
                // getting values from selected ListItem
                //String reqid = ((TextView) view.findViewById(R.id.reqid)).getText()
                // .toString();
                //String chpuid = ((TextView) view.findViewById(R.id.chpuid)).getText()
                       // .toString();
                //String chduid = ((TextView) view.findViewById(R.id.chduid)).getText()
                        //.toString();
                //String bkprefinfo = ((TextView) view.findViewById(R.id.bkprefinfo)).getText()
                // .toString();
                //String bkstatusinfo = ((TextView) view.findViewById(R.id.statusinfo)).getText()
                //.toString();
                //String bkthumbnail = ((TextView) view.findViewById(R.id.bkimagelink)).getText().toString();

                //String hpaddress = ((TextView) view.findViewById(R.id.bkhpaddress)).getText().toString();
                //String hospitalname = ((TextView) view.findViewById(R.id.bkhospitalname)).getText().toString();

                //Log.e("hp", "received hp address from listview : "+hpaddress);

                // Starting new intent
            //    Intent in = new Intent(getApplicationContext(),
        //                MessagingActivity.class);
                // sending pid to next activity
           //     in.putExtra("puid", chpuid);
           //     in.putExtra("duid", chduid);
                //in.putExtra("bktimeinfo", bktimeinfo);
                //in.putExtra("bkprefinfo", bkprefinfo);
                // in.putExtra("bkstatusinfo", bkstatusinfo);
                //in.putExtra("bkthumbnail", bkthumbnail);
                //in.putExtra("bkhpaddress", hpaddress);
                //in.putExtra("bkhospitalname", hospitalname);




                // starting new activity and expecting some response back
             //   startActivityForResult(in, 100);
           // }
    //    });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        get_convos(puid,duid);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("newmessage"));

    }


    private boolean sendChatMessage(String puid,String duid,String message,String name) {
        Convos con = new Convos();
        con.setCnmssg(message);
        con.setCnsndname(name+":");
        cnList.add(con);
        adapter.notifyDataSetChanged();
        sendmssg(puid,duid,message);

        return true;
    }



    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           // get_convos(ptid,dtid);
            String action = intent.getAction();
            String message=intent.getStringExtra("message");
            String mssg=intent.getStringExtra("mssg");
            String sid=intent.getStringExtra("sid");
            String sname=intent.getStringExtra("sname");


            Log.e("broadlog","Inside onreceive broadcast");
            Log.e("broadlog","The NOTE MESSAGE IS " + message);
            Log.e("broadlog","the new convo message is:  " + mssg);
            Log.e("broadlog","the receiving USERID IS: " + sid);

            if(sid.equals(dtid)) {
                Convos con = new Convos();
                con.setCnmssg(mssg);
                con.setCnsndname(sname+":");
                cnList.add(con);
                adapter.notifyDataSetChanged();
                adapter.registerDataSetObserver(new DataSetObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        Log.e("adapterobserver", "RECEIVE: Inside onChanged()");
                        listView.setSelection(adapter.getCount() - 1);
                    }
                });
            }



           // Toast.makeText(getApplicationContext(),
           //         "The Received Message is:"+message, Toast.LENGTH_LONG).show();

            //  ... react to local broadcast message
        }
    };



    public void get_convos(final String puid ,final String duid){
        Log.e("viewbook", "before volley request");
        // changing action bar color


        // Creating volley request obj
        adapter.clearData();
        adapter.notifyDataSetChanged();
        String tag_string_req = "req_login";
        Log.e("viewbook", "code to clear cache is below");
        AppController.getInstance().getRequestQueue().getCache().remove(convosurl);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                convosurl, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
               hidePDialog();
                try {

                    Log.e("viewbook", "inside onresponse- Response String is: "+response);





                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");


                    if (success) {

                        JSONArray chatsarray = jObj.getJSONArray("row");


                        Log.e("viewbook", "inside onresponse Array is: " + chatsarray);
// Parsing json
                        for (int i = 0; i < chatsarray.length(); i++) {
                            try {
                                Log.e("viewbook", "inside response loop");
                                JSONObject obj = chatsarray.getJSONObject(i);
                                Convos con = new Convos();
                                con.setCnmssg(obj.getString("mssg"));
                                con.setCnsndname(obj.getString("sname")+":");

                            //    chat.setPuid(obj.getString("puid"));
                            //    chat.setDuid(obj.getString("duid"));
                             //   chat.setMssg(obj.getString("mssg"));
                             //   chat.setMsgid(obj.getString("mid"));
                             //   chat.setStime(obj.getString("sendtime"));



                                cnList.add(con);
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
                params.put("user_id", puid);
                params.put("reciepent_id", duid);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }










    private void sendmssg(final String puid, final String duid, final String mssg) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

     //   pDialog.setMessage("Requesting ...");
//        showDialog();
        Log.e("mssg", "in sendmssg() before post");
        final String sendmssg_api="http://azmediame.net/med360/webinterface/api/send_message.php";
        Log.e("bookapp", "code to clear cache is below");
        AppController.getInstance().getRequestQueue().getCache().remove(sendmssg_api);
        StringRequest strReq2 = new StringRequest(Request.Method.POST,sendmssg_api, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("mssg", "Send Message Response: " + response.toString());
               // hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);

                    //boolean error = jObj.getBoolean("error");
                    String success = jObj.getString("success");
                    if (success.equals("1")) {
                        // Request stored in MySQL

                        Toast.makeText(getApplicationContext(), "Message Sent Successfully", Toast.LENGTH_LONG).show();

                        // Launch doctor profile activity
                        // Starting new intent
                       // Intent in = new Intent(getApplicationContext(),
                        //        ConversationActivity.class);
                        // sending pid to next activity
                      //  in.putExtra("puid", puid);
                       // in.putExtra("duid", duid);

                        // starting new activity and expecting some response back
                    //    startActivityForResult(in, 100);

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
               // hideDialog();
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


    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}



